package co.com.management.jpa.persistence.client;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.NoDataFoundException;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ClientRepositoryImpl extends AdapterOperations<Client, ClientDao, String, ClientDaoRepository>
        implements ClientRepository
{

    public ClientRepositoryImpl(ClientDaoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> {
            return mapper.map(d, Client.class);
        });
    }

    @Override
    public Client saveClient(Client client) {
        return toEntity(repository.save(toData(client)));
    }

    @Override
    public Client deleteClient(String id) {
        Client client = findById(id);
        if(Objects.nonNull(client)){
            repository.deleteById(id);
            return client;
        }
        throw new NoDataFoundException();
    }

    @Override
    public Client findById(String id) {
        return repository.findById(id).map(this::toEntity)
                .orElseThrow(null);
    }

    @Override
    public Client findByDocumentNumberAndDocumentType(String documentNumber,
                                                      String documentType) {
        ClientDao client = repository.findByDocumentNumberAndDocumentType(documentNumber,
                documentType).orElse(null);
        return this.toEntity(client);

    }

    @Override
    public PageResult<Client> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientDao> clientsDao = repository.findAll(pageable);
        if(clientsDao.isEmpty()) throw new DataFoundException("No hay información");
        List<Client> clients = clientsDao.getContent().stream()
                .map(this::toEntity)
                .toList();
        return new PageResult<>(
                clients,
                clientsDao.getNumber(),
                clientsDao.getSize(),
                clientsDao.getTotalElements(),
                clientsDao.getTotalPages(),
                clientsDao.hasNext(),
                clientsDao.hasPrevious()
        );
    }

}