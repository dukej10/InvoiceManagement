package co.com.management.api.dto.mappers;

import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.api.dto.models.request.ClientFullDTO;
import co.com.management.model.client.Client;
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
