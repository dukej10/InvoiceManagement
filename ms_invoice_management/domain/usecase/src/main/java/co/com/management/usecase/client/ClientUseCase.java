package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.GeneralException;
import co.com.management.model.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;

    public Client saveClient(Client client){
        Client clientFound = findByInfoDocument(client.getDocumentNumber(), client.getDocumentType());
        if(Objects.nonNull(clientFound)) throw new DataFoundException("El cliente");
        try {
            return clientRepository.saveClient(client);
        } catch (Exception e) {
            throw new GeneralException("Error al guardar el cliente");
        }
    }

    public Client updateClient(Client client){
        Client clientFound = getClientById(client.getId());
        if(Objects.isNull(clientFound)) throw new NoDataFoundException();
        client.setCreatedDate(clientFound.getCreatedDate());

        try {
            return clientRepository.saveClient(client);
        } catch (Exception e) {
            throw new GeneralException("Error al actualizar el cliente");
        }
    }


    public Client getClientById(UUID id) {
        try {
            return  clientRepository.findById(id);
        } catch (Exception e) {
            throw new GeneralException("Error al obtener el cliente");
        }
    }

    public Client findByInfoDocument(String documentNumber, String documentType){
        try {
            return clientRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType);
        } catch (Exception e) {
            throw new GeneralException("Error al obtener el cliente");
        }
    }

    public PageResult<Client> getAll(int size, int page) {
        try {
            return clientRepository.findAll(page, size);
        }catch (Exception e) {
            throw new GeneralException("Error al obtener los clientes");

        }
    }

    public Client deleteById(UUID id) {
        try {
            Client client = getClientById(id);
            return clientRepository.deleteClient(id);
        } catch (Exception e) {
            throw new GeneralException("Error al eliminar el cliente");
        }
    }

}
