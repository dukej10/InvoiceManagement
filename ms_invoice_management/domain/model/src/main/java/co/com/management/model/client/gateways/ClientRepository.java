package co.com.management.model.client.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;

import java.util.UUID;

public interface ClientRepository {
    Client saveClient(Client client);

    Client deleteClient(UUID id);

    Client findById(UUID id);

    Client findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    PageResult<Client> findAll(int page, int size);

}
