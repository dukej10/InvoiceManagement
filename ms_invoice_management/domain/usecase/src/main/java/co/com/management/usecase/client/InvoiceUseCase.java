package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.calculatedinvoice.CalculatedInvoice;
import co.com.management.model.calculatedinvoice.gateways.CalculatedInvoiceRepository;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.model.product.Product;
import co.com.management.model.product.ProductItem;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class InvoiceUseCase {
    private final InvoiceRepository invoiceRepository;
    private final ClientUseCase clientUseCase;
    private final CalculatedInvoiceRepository calculatedInvoiceRepository;

    public Invoice saveInvoice(Invoice invoice, String code){
        Client clientFound = clientUseCase.getClientById(code);
        String invoiceCode = UUID.randomUUID().toString();
        invoice.setId(invoiceCode);
        CalculatedInvoice calculatedInvoice = toCalculatedInvoice(invoice);
        Invoice aux = calculatedInvoiceRepository.calculateInvoice(calculatedInvoice);
        invoice.setTotalAmount(aux.getTotalAmount());
        return invoiceRepository.registerInvoice(invoice, clientFound);

    }

    public PageResult<Invoice> getAllByClientId(String clientId, int page, int size) {
        return invoiceRepository.getAllByClientId(clientId, page, size);
    }

    public PageResult<Invoice> getAllInvoices(int page, int size) {
        return invoiceRepository.getAllPageable(page, size);
    }

    private CalculatedInvoice toCalculatedInvoice(Invoice invoice) {
            return  CalculatedInvoice.builder()
                    .code(invoice.getId())
                    .createdDate(invoice.getCreatedDate())
                    .products(invoice.getProducts().stream()
                            .map(this::toProductItem)
                            .toList())
                    .clientId(invoice.getClientId())
                    .build();
    }

    private ProductItem toProductItem(Product product) {
        return ProductItem.builder()
                .name(product.getName())
                .quantity(product.getQuantity())
                .unitPrice(product.getUnitPrice())
                .build();
    }

    public void deleteById(String id) {
        invoiceRepository.deleteInvoice(id);
    }

    public Invoice getById(String id) {
        return invoiceRepository.getById(id);
    }
}
