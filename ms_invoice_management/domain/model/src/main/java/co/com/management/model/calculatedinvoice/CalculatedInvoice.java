package co.com.management.model.calculatedinvoice;

import co.com.management.model.product.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
