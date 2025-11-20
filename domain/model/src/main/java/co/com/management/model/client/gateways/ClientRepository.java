package co.com.management.model.client.gateways;

import co.com.management.model.client.Client;

import java.util.List;

public interface ClientRepository {
    Client saveClient(Client Client);

    List<Client> getAll();

    Boolean deleteClient();

    Client findById();
}
