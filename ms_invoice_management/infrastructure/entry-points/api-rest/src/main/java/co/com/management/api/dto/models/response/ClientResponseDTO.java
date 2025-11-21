package co.com.management.api.dto.models.response;

import co.com.management.model.invoice.Invoice;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Builder(toBuilder = true)
public class ClientResponseDTO {
    private String documentNumber;
    private String documentType;
    private Boolean state;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<Invoice> invoices;
}
