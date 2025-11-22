package co.com.management.model.calculatedinvoice.gateways;

import co.com.management.model.calculatedinvoice.CalculatedInvoice;
import co.com.management.model.invoice.Invoice;

public interface CalculatedInvoiceRepository {
    Invoice calculateInvoice(CalculatedInvoice request);
}
