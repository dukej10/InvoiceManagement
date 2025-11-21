package co.com.management.api.controllers;


import co.com.management.api.Utility;
import co.com.management.api.dto.mappers.RequestMapper;
import co.com.management.api.dto.mappers.ResponseMapper;
import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.api.dto.models.request.InvoiceDTO;
import co.com.management.usecase.client.ClientUseCase;
import co.com.management.usecase.client.InvoiceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    @PostMapping(path = "/create")
    public ResponseEntity<?> save(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        var client = invoiceUseCase.saveInvoice(RequestMapper.toModel(invoiceDTO), invoiceDTO.getClientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                client
        );
    }

}

