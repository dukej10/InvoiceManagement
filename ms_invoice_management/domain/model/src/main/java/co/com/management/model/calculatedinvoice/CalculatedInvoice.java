package co.com.management.model.calculatedinvoice;
import co.com.management.model.dsfs;
import co.com.management.model.product.Product;
import co.com.management.model.product.ProductItem;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class CalculatedInvoice {
    String code;
    LocalDateTime createdDate;
    String clientId;

    List<ProductItem> products;
}
