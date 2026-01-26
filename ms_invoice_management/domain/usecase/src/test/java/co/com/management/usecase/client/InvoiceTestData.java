package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class InvoiceTestData {

    private InvoiceTestData() {}

    public static final String CLIENT_ID = "cli-12345";

    public static Client aClient(String id) {
        return Client.builder()
                .id(id)
                .documentType("CC")
                .documentNumber("123456789")
                .firstName("Juan")
                .lastName("Pérez")
                .build();
    }

    public static Invoice anInvoiceInput(String clientId) {
        return Invoice.builder()
                .clientId(clientId)
                .createdDate(LocalDateTime.now())
                .products(List.of(
                        Product.builder()
                                .name("Coca-Cola")
                                .quantity(2)
                                .unitPrice(5_000.0)
                                .build(),
                        Product.builder()
                                .name("Papas")
                                .quantity(1)
                                .unitPrice(10_000.0)
                                .build()
                ))
                .build();
    }

    public static Invoice anInvoiceWithTotal(String clientId, BigDecimal total) {
        return Invoice.builder()
                .id("any-id")
                .clientId(clientId)
                .createdDate(LocalDateTime.now())
                .totalAmount(total)
                .products(List.of(
                        Product.builder().name("X").quantity(1).unitPrice(1.0).build()
                ))
                .build();
    }

    public static PageResult<Invoice> aPageResultInvoices() {
        return PageResult.<Invoice>builder()
                .items(List.of(anInvoiceWithTotal(CLIENT_ID, BigDecimal.valueOf(10.0))))
                .page(0)
                .size(10)
                .totalItems(1L)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

}

