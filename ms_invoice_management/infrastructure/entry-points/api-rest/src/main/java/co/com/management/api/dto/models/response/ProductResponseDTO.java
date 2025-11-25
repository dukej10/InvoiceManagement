package co.com.management.api.dto.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductResponseDTO {

    private  String code;

    private String name;

    private Integer quantity;

    private Double unitPrice;
}
