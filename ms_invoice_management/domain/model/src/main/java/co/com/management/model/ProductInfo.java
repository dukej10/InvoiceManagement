package co.com.management.model;

public record  ProductInfo(String[] names,
                           Integer[] quantities,
                           Float[] prices,
                           String[] codes
) {
}
