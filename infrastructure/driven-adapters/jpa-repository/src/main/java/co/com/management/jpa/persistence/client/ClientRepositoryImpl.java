package co.com.management.jpa.persistence.client;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class ClientRepositoryImpl extends AdapterOperations<Client, ClientDao, UUID, ClientDaoRepository>
        implements ClientRepository
{

    public ClientRepositoryImpl(ClientDaoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> {
            return mapper.map(d, Client.class);
        });
    }

    @Override
    public Client saveClient(Client client) {
        return super.save(client);
    }


    @Override
    public List<Client> getAll() {
        return super.findAll();
    }

    @Override
    public Boolean deleteClient(UUID id) {
        Client client = super.findById(id);
        if(Objects.isNull(client)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Client findById() {
        return null;
    }

}