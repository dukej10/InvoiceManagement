package co.com.management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class  ProductInfo {
    private String[] names;
    private Integer[] quantities;
    private Double[] prices;
    private String[] codes;
}
