package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.GeneralException;
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
        if(Objects.nonNull(clientFound)) throw new DataFoundException("El cliente");
        try {
            return invoiceRepository.registerInvoice(invoice);
        } catch (Exception e) {
            throw new GeneralException("Error al registrar la factura");
        }
    }

    public PageResult<Invoice> getAllByIdCliente(UUID idClient, int size, int page) {
        Client clientFound = clientUseCase.getClientById(idClient);
        if(Objects.nonNull(clientFound)) throw new DataFoundException("El cliente");
        try {
            return invoiceRepository.getAllByIdCliente(idClient, size, page);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener las facturas del cliente");

        }
    }

    public PageResult<Invoice> getAllInvoices(UUID idClient, int size, int page) {
        try {
            return invoiceRepository.getAllByIdCliente(idClient, size, page);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener las facturas");

        }
    }
}
