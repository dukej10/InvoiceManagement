package co.com.management.api.dto.mappers;

import co.com.management.api.dto.models.response.ClientResponseDTO;
import co.com.management.api.dto.models.response.ClientResponseFullDTO;
import co.com.management.api.dto.models.response.InvoiceResponseDTO;
import co.com.management.api.dto.models.response.PageResultDTO;
import co.com.management.api.dto.models.response.ProductResponseDTO;
import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMapper {
    public ClientResponseFullDTO responseFull(Client client){
        return  ClientResponseFullDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .state(client.getState())
                .documentType(client.getDocumentType())
                .documentNumber(client.getDocumentNumber())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .createdDate(client.getCreatedDate())
                .updatedDate(client.getUpdatedDate())
                .build();
    }

    public ClientResponseDTO response(Client client){
        return  ClientResponseDTO.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .state(client.getState())
                .documentType(client.getDocumentType())
                .documentNumber(client.getDocumentNumber())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .build();
    }

    public InvoiceResponseDTO response(Invoice invoice){
        return  InvoiceResponseDTO.builder()
                .code(invoice.getId())
                .clientId(invoice.getClientId())
                .products(invoice.getProducts().stream()
                        .map(ResponseMapper::responseFull).toList())
                .createdDate(invoice.getCreatedDate())
                .build();
    }

    private ProductResponseDTO responseFull(Product product){
        return ProductResponseDTO.builder()
                .code(product.getCode())
                .name(product.getName())
                .quantity(product.getQuantity())
                .unitPrice(product.getUnitPrice())
                .build();
    }

    public PageResultDTO<ClientResponseFullDTO> toPageResultClientDTO(PageResult<Client> pageResult){
        return new PageResultDTO<> (
                pageResult.getItems().stream().map(ResponseMapper::responseFull).toList(),
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalItems(),
                pageResult.getTotalPages(),
                pageResult.isHasNext(),
                pageResult.isHasPrevious()
        );
    }

    public PageResultDTO<InvoiceResponseDTO> toPageResultInvoiceDTO(PageResult<Invoice> pageResult){
        return new PageResultDTO<> (
                pageResult.getItems().stream().map(ResponseMapper::response).toList(),
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalItems(),
                pageResult.getTotalPages(),
                pageResult.isHasNext(),
                pageResult.isHasPrevious()
        );
    }
}
