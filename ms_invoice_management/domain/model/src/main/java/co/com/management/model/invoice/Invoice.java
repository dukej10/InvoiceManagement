package co.com.management.model.invoice;

import co.com.management.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Invoice {
    private String id;
    private Double totalAmount;
    private List<Product> products;
    private LocalDateTime createdDate;
    private String clientId;
}
