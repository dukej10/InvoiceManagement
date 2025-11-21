package co.com.management.api.dto.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceDTO {

    private UUID clientId;

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String documentType;

    @Valid
    List<ProductDTO> products;
}
