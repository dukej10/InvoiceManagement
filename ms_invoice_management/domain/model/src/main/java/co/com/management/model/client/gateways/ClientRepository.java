package co.com.management.model.client.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;

public interface ClientRepository {
    Client saveClient(Client client);

    Client deleteClient(String id);

    Client findById(String id);

    Client findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    PageResult<Client> findAll(int page, int size);

}
