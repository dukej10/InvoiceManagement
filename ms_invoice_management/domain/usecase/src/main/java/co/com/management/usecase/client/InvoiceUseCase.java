package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.exception.GeneralException;
import co.com.management.model.exception.NoDataFoundException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class InvoiceUseCase {
    private final InvoiceRepository invoiceRepository;
    private final ClientUseCase clientUseCase;

    public Invoice saveInvoice(Invoice invoice, UUID id){
        Client clientFound = clientUseCase.getClientById(id);
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
}
