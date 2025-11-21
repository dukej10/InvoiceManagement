package co.com.management.api.dto.mappers;

import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.api.dto.models.request.ClientFullDTO;
import co.com.management.api.dto.models.request.InvoiceDTO;
import co.com.management.api.dto.models.request.ProductDTO;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {

    public Client toModel(ClientDTO clientDTO){
        return Client.builder()
                .documentNumber(clientDTO.getDocumentNumber())
                .state(clientDTO.getState())
                .email(clientDTO.getEmail())
                .address(clientDTO.getAddress())
                .phone(clientDTO.getPhone())
                .documentType(clientDTO.getDocumentType())
                .lastName(clientDTO.getLastName())
                .firstName(clientDTO.getFirstName())
                .build();
    }

    public Invoice toModel(InvoiceDTO invoiceDTO){
        return Invoice.builder()
                .products(
                        invoiceDTO.getProducts().stream()
                                .map(RequestMapper::toModel)
                                .toList()
                )
                .build();
    }

    private Product toModel(ProductDTO productDTO){
        return Product.builder()
                .name(productDTO.getName())
                .unitPrice(productDTO.getUnitPrice())
                .quantity(productDTO.getQuantity())
                .build();
    }

    public Client toModelFull(ClientFullDTO clientDTO){
        return Client.builder()
                .id(clientDTO.getId())
                .state(clientDTO.getState())
                .documentNumber(clientDTO.getDocumentNumber())
                .email(clientDTO.getEmail())
                .address(clientDTO.getAddress())
                .phone(clientDTO.getPhone())
                .documentType(clientDTO.getDocumentType())
                .lastName(clientDTO.getLastName())
                .firstName(clientDTO.getFirstName())
                .build();
    }
}
