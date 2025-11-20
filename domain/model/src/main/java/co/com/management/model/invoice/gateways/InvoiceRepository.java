package co.com.management.model.invoice.gateways;

import co.com.management.model.invoice.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository {
    Invoice saveInvoice(Invoice invoice);

    List<Invoice> getAll();

    Boolean deleteInvoice();

    Invoice findById(UUID id);
}

