package co.com.management.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AmountResponseDTO {

    @JsonProperty("data")
    private InvoiceData data;

    private String message;
    private String timestamp;

    private Integer statusCode;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceData {
        private String code;
        private BigDecimal totalAmount;
        private List<ProductItem> products;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductItem {
        private String name;
        private Integer quantity;

        @JsonProperty("unitPrice")
        private BigDecimal unitPrice;
    }
}
