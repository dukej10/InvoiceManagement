package co.com.management.consumer;

import co.com.management.consumer.dto.AmountRequestDTO;
import co.com.management.consumer.dto.AmountResponseDTO;
import co.com.management.consumer.mapper.ResponseMapper;
import co.com.management.model.calculatedinvoice.CalculatedInvoice;
import co.com.management.model.calculatedinvoice.gateways.CalculatedInvoiceRepository;
import co.com.management.model.invoice.Invoice;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestConsumer implements CalculatedInvoiceRepository {

    private final RestClient restClient;
    private final ResponseMapper responseMapper;

    @Override
    @CircuitBreaker(name = "externalInvoiceService", fallbackMethod = "fallbackCalculateInvoice")
    public Invoice calculateInvoice(CalculatedInvoice request) {
        log.info("Calculando factura para cliente: {}", request.getClientId());

        AmountRequestDTO dtoRequest = responseMapper.toDTO(request);

        AmountResponseDTO response = restClient
                .post()
                .uri("/calculate")
                .body(dtoRequest)
                .retrieve()
                .toEntity(AmountResponseDTO.class)
                .getBody();

        log.info("Cálculo exitoso. Total: {}", response.getData().getTotalAmount());

        return responseMapper.toCalculatedInvoice(response, request.getClientId());
    }

    public Invoice fallbackCalculateInvoice(CalculatedInvoice request, Throwable throwable) {
        log.warn("Fallback activado para cliente {} → {}", request.getClientId(), throwable.getMessage());

        return Invoice.builder()
                .clientId(request.getClientId())
                .totalAmount(0.0)
                .build();
    }
}