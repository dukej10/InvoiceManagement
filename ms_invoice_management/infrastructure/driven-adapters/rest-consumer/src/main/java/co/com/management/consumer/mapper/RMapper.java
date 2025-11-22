package co.com.management.consumer.mapper;

import co.com.management.consumer.dto.AmountRequestDTO;
import co.com.management.consumer.dto.AmountResponseDTO;
import co.com.management.model.calculatedinvoice.CalculatedInvoice;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RMapper {

    public Invoice toCalculatedInvoice(AmountResponseDTO dto, String clientId) {

        var data = dto.getData();

        List<Product> products = data.getProducts().stream()
                .map(p -> Product.builder()
                        .code(p.getCode())
                        .name(p.getName())
                        .quantity(p.getQuantity())
                        .unitPrice(p.getUnitPrice())
                        .build())
                .toList();

        AmountResponseDTO.InvoiceData invoiceData = dto.getData();
        return Invoice.builder()
                .code(invoiceData.getCode())
                .createdDate(invoiceData.getCreatedDate())
                .products(products)
                .clientId(clientId)
                .totalAmount(invoiceData.getTotalAmount())
                .build();

    }
    public AmountRequestDTO toDTO(CalculatedInvoice calculatedInvoice) {

        List<AmountRequestDTO.ProductItem> fluid = calculatedInvoice.getProducts().stream()
                .map(p -> AmountRequestDTO.ProductItem.builder()
                        .name(p.getName())
                        .quantity(p.getQuantity())
                        .unitPrice(p.getUnitPrice())
                        .build())
                .toList();

        // ENVÍO PLANO → igual que espera FastAPI
        return AmountRequestDTO.builder()
                .code(calculatedInvoice.getCode())    // ← directo
                .products(fluid)                   // ← directo
                .build();
    }

}
