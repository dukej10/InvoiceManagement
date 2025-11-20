package co.com.management.api.dto.models.request;

import co.com.management.model.invoice.Invoice;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientDTO {

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String documentType;

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
