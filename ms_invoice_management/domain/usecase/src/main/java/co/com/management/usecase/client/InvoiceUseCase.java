package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.calculatedinvoice.CalculatedInvoice;
import co.com.management.model.calculatedinvoice.gateways.CalculatedInvoiceRepository;
import co.com.management.model.client.Client;
import co.com.management.model.exception.GeneralException;
import co.com.management.model.exception.NoDataFoundException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.model.product.Product;
import co.com.management.model.product.ProductItem;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class InvoiceUseCase {
    private final InvoiceRepository invoiceRepository;
    private final ClientUseCase clientUseCase;
    private final CalculatedInvoiceRepository calculatedInvoiceRepository;

    public Invoice saveInvoice(Invoice invoice, UUID id){
        Client clientFound = clientUseCase.getClientById(id);
        String invoiceCode = UUID.randomUUID().toString();
        invoice.setCode(invoiceCode);
        CalculatedInvoice calculatedInvoice = toCalculatedInvoice(invoice);
        Invoice aux = calculatedInvoiceRepository.calculateInvoice(calculatedInvoice);
        invoice.setTotalAmount(aux.getTotalAmount());
        if(Objects.isNull(clientFound)) throw new NoDataFoundException();
        try {
            return invoiceRepository.registerInvoice(invoice, clientFound);
        } catch (Exception e) {
            throw new GeneralException("Error al registrar la factura");
        }
    }

    public PageResult<Invoice> getAllByClientId(UUID clientId, int page, int size) {
        Client clientFound = clientUseCase.getClientById(clientId);
        if(Objects.isNull(clientFound)) throw new NoDataFoundException();
        try {
            return invoiceRepository.getAllByClientId(clientId.toString(), page, size);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener las facturas del cliente");

        }
    }

    public PageResult<Invoice> getAllInvoices(int page, int size) {
        try {
            return invoiceRepository.getAll(page, size);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener las facturas");

        }
    }

    private CalculatedInvoice toCalculatedInvoice(Invoice invoice) {
            return  CalculatedInvoice.builder()
                    .code(invoice.getCode())
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
}
