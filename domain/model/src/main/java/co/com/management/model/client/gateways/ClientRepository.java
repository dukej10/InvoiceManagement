package co.com.management.model.client.gateways;

import co.com.management.model.client.Client;

import java.util.List;
import java.util.UUID;

public interface ClientRepository {
    Client saveClient(Client Client);

    List<Client> getAll();

    Boolean deleteClient(UUID id);

    Client findById();
}
