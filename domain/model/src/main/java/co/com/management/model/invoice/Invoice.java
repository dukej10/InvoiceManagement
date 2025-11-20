package co.com.management.model.invoice;
import co.com.management.model.product.Product;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Invoice {
    private UUID code;
    private Double totalAmount;
    private List<Product> products;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
