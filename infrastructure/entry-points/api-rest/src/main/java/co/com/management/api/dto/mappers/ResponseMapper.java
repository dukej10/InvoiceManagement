package co.com.management.api.dto.mappers;

import co.com.management.api.dto.models.response.ClientResponseDTO;
import co.com.management.api.dto.models.response.ClientResponseFullDTO;
import co.com.management.model.client.Client;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMapper {
    public ClientResponseFullDTO responseFull(Client client){
        return  ClientResponseFullDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .documentType(client.getDocumentType())
                .documentNumber(client.getDocumentNumber())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .createdDate(client.getCreatedDate())
                .updatedDate(client.getUpdatedDate())
                //.invoices()
                .build();
    }

    public ClientResponseDTO response(Client client){
        return  ClientResponseDTO.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .documentType(client.getDocumentType())
                .documentNumber(client.getDocumentNumber())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .build();
    }
}
