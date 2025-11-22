package co.com.management.model;

public record  ProductInfo(String[] names,
                           Integer[] quantities,
                           Double[] prices,
                           String[] codes
) {
}
