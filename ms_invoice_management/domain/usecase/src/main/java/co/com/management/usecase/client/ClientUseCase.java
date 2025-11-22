package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.GeneralException;
import co.com.management.model.exception.NoDataFoundException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;

    public Client saveClient(Client client){
        Client clientFound = findByInfoDocument(client.getDocumentNumber(), client.getDocumentType());
        if(Objects.nonNull(clientFound)) throw new DataFoundException("El cliente ya se encuentra registrado");
        return clientRepository.saveClient(client);
    }

    public Client updateClient(Client client){
        Client clientFound = getClientById(client.getId());
        client.setCreatedDate(clientFound.getCreatedDate());
        Client clientUpdated = clientRepository.saveClient(client);
        return requireNonNull(clientUpdated);
    }


    public Client getClientById(UUID id) {
       Client clientFound = clientRepository.findById(id);
       return requireNonNull(clientFound);
    }

    public Client findByInfoDocument(String documentNumber, String documentType) {
       return clientRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType);

    }

    public PageResult<Client> getAll(int size, int page) {
            return clientRepository.findAll(page, size);
    }

    public Client deleteById(UUID id) {
        Invoice invoiceDeleted = deleteInvoice(id);
        Client clientDeleted =  clientRepository.deleteClient(id);
        if(Objects.isNull(clientDeleted) && Objects.isNull(invoiceDeleted)) {
            throw new NoDataFoundException();
        }
        return requireNonNull(clientDeleted);
    }

    private Invoice deleteInvoice(UUID id) {
        Invoice invoiceFound = findInvoiceByClientId(id);
        if(Objects.isNull(invoiceFound)) return null;
        return invoiceRepository.deleteInvoice(invoiceFound.getCode());
    }

    private Invoice findInvoiceByClientId(UUID clientId) {
        return invoiceRepository.findByClientId(clientId.toString());
    }

    public  Client requireNonNull(Client model) {
        if(Objects.isNull(model)) throw new NoDataFoundException();
        return model;
    }

}
