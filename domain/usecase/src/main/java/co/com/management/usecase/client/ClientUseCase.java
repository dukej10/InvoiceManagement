package co.com.management.usecase.client;

import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;

    public Client saveClient(Client client){
        try {
            return clientRepository.saveClient(client);
        } catch (Exception e) {
            return null;
        }
    }
}
