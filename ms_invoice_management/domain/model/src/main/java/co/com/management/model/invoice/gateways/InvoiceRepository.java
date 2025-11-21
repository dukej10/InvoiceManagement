package co.com.management.model.invoice.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;

public interface InvoiceRepository {

    PageResult<Invoice> getAllByClientId(String id, int page, int size);

    PageResult<Invoice> getAll(int page, int size);

    Invoice registerInvoice(Invoice invoice, Client client);

    Invoice deleteInvoice(String id);

    Invoice findByClientId(String id);
}

