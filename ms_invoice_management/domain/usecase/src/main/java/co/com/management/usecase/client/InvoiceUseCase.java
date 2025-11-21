package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.exception.DataFoundException;
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
        Client clientFound = getClient(id, invoice.getDocumentNumber(),
                invoice.getDocumentType());
        if(Objects.isNull(clientFound)) throw new NoDataFoundException();
        try {
            return invoiceRepository.registerInvoice(invoice, clientFound);
        } catch (Exception e) {
            throw new GeneralException("Error al registrar la factura");
        }
    }

    public PageResult<Invoice> getAllByIdCliente(UUID clientId, int size, int page) {
        Client clientFound = clientUseCase.getClientById(clientId);
        if(Objects.nonNull(clientFound)) throw new DataFoundException("El cliente");
        try {
            return invoiceRepository.getAllByClientId(clientId.toString(), size, page);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener las facturas del cliente");

        }
    }

    public PageResult<Invoice> getAllInvoices(UUID clientId, int size, int page) {
        try {
            return invoiceRepository.getAllByClientId(clientId.toString(), size, page);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener las facturas");

        }
    }

    private Client getClient(UUID id, String documentNumber, String documentType) {
        if(Objects.nonNull(id)) {
            return clientUseCase.getClientById(id);
        } else {
            return clientUseCase.findByInfoDocument(documentNumber, documentType);
        }
    }
}
