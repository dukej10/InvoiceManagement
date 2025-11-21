package co.com.management.jpa.helper;

import co.com.management.jpa.persistence.invoice.InvoiceDao;
import co.com.management.jpa.persistence.invoice.ProductDao;
import co.com.management.model.PageResult;
import co.com.management.model.ProductInfo;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@UtilityClass
public class Util {

    public static <T> PageResult<T> structureResponse(List<T> items, Page<?> itemsFound) {
        return new PageResult<>(
                items,
                itemsFound.getNumber(),
                itemsFound.getSize(),
                itemsFound.getTotalElements(),
                itemsFound.getTotalPages(),
                itemsFound.hasNext(),
                itemsFound.hasPrevious()
        );
    }

    public ProductInfo structProduct(List<Product> products) {
        String[] names = products.stream().map(Product::getName).toArray(String[]::new);
        Integer[] quantities = products.stream().map(Product::getQuantity).toArray(Integer[]::new);
        Float[] prices = products.stream().map(Product::getUnitPrice).toArray(Float[]::new);
        String[] codes = products.stream().map(p -> UUID.randomUUID().toString()).toArray(String[]::new);

        IntStream.range(0, products.size())
                .forEach(i -> products.get(i).setCode(codes[i]));

        return new ProductInfo(names, quantities, prices, codes);
    }

    public Map<String, Object> structureParams(Invoice invoice, Client client, ProductInfo productData) {
        return Map.of(
                "p_invoice_code", invoice.getCode(),
                "p_create_date", Timestamp.valueOf(invoice.getCreatedDate()),
                "p_total_amount", BigDecimal.valueOf(invoice.getTotalAmount()),
                "p_client_id", client.getId(),
                "p_insert_invoice", client.getState() ? 1 : 0,
                "p_products_code", new OracleHelper("INVOICE.VARCHAR2_LIST", productData.codes()),
                "p_products_name", new OracleHelper("INVOICE.VARCHAR2_LIST", productData.names()),
                "p_products_quantity", new OracleHelper("INVOICE.NUMBER_LIST", productData.quantities()),
                "p_products_unit_price", new OracleHelper("INVOICE.FLOAT_LIST", productData.prices())
        );
    }

    public Invoice toModel(InvoiceDao invoiceDao) {
        return Invoice.builder()
                .code(invoiceDao.getCode())
                .clientId(invoiceDao.getClientId())
                .createdDate(invoiceDao.getCreatedDate())
                .totalAmount(invoiceDao.getTotalAmount())
                .products(
                        invoiceDao.getProducts().stream()
                                .map(Util::toModel
                                ).toList()
                )
                .build();
    }

private Product toModel(ProductDao productDao) {
    return Product.builder()
            .code(productDao.getCode())
            .name(productDao.getName())
            .quantity(productDao.getQuantity())
            .unitPrice(productDao.getUnitPrice())
            .build();
}
}
