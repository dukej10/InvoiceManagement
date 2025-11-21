package co.com.management.model.invoice.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository {

    PageResult<Invoice> getAllByClientId(String id, int page, int size);

    PageResult<Invoice> getAll(int page, int size);

    Invoice findById(String id);

    Invoice registerInvoice(Invoice invoice, Client client);
}

