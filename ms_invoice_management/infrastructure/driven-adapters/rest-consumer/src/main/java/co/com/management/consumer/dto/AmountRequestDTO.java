package co.com.management.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmountRequestDTO {

    private InvoiceData data;

    private String message;

    @JsonProperty("statusCode")
    private Integer statusCode;

    private LocalDateTime timestamp;

    @Data
    public static class InvoiceData {
        private String code;
        private List<ProductItem> products;
        private LocalDateTime createdDate;
    }

    @Data
    public static class ProductItem {
        private String name;
        private Integer quantity;
        private Float unitPrice;
    }
}
