package co.com.management.api.dto.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientFullDTO {

    @NotNull
    private UUID id;

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String documentType;

    @NotNull
    private Boolean state;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;


    @NotBlank
    private String phone;

    @NotBlank
    private String address;

}
