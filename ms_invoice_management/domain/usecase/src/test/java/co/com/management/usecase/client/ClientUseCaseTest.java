package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.NoDataFoundException;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private ClientUseCase clientUseCase;

    private static final String CLIENT_ID = "cli-12345";
    private static final String DOCUMENT_NUMBER = "123456789";
    private static final String DOCUMENT_TYPE = "CC";

    @Test
    @DisplayName("saveClient: debe guardar el cliente exitosamente cuando no existe previamente")
    void saveClient_success() {
        Client client = Client.builder()
                .documentNumber(DOCUMENT_NUMBER)
                .documentType(DOCUMENT_TYPE)
                .firstName("Juan Diego")
                .lastName("Pérez")
                .build();

        Client savedClient = client.toBuilder()
                .id(CLIENT_ID)
                .build();

        when(clientRepository.findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE))
                .thenReturn(null);
        when(clientRepository.saveClient(client))
                .thenReturn(savedClient);

        Client result = clientUseCase.saveClient(client);

        assertNotNull(result);
        assertEquals(CLIENT_ID, result.getId());

        verify(clientRepository).findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE);
        verify(clientRepository).saveClient(client);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("saveClient: debe lanzar DataFoundException cuando ya existe un cliente con el mismo documento")
    void saveClient_throwsDataFoundException_whenClientExists() {
        // Arrange
        Client existingClient = Client.builder()
                .id(CLIENT_ID)
                .documentNumber(DOCUMENT_NUMBER)
                .documentType(DOCUMENT_TYPE)
                .build();

        when(clientRepository.findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE))
                .thenReturn(existingClient);

        Client newClient = Client.builder()
                .documentNumber(DOCUMENT_NUMBER)
                .documentType(DOCUMENT_TYPE)
                .build();

        DataFoundException ex = assertThrows(DataFoundException.class, () -> clientUseCase.saveClient(newClient));
        assertEquals("El cliente ya se encuentra registrado", ex.getMessage());

        verify(clientRepository).findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE);
        verify(clientRepository, never()).saveClient(any());
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("updateClient: debe actualizar el cliente manteniendo la fecha de creación original")
    void updateClient_success() {
        LocalDateTime originalDate = LocalDateTime.now().minusDays(1);

        Client existing = Client.builder()
                .id(CLIENT_ID)
                .firstName("Old Name")
                .createdDate(originalDate)
                .build();

        Client updatedInput = existing.toBuilder()
                .firstName("New Name")
                .createdDate(LocalDateTime.now())
                .build();

        when(clientRepository.findById(CLIENT_ID)).thenReturn(existing);
        when(clientRepository.saveClient(any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Client result = clientUseCase.updateClient(updatedInput);

        assertNotNull(result);
        assertEquals("New Name", result.getFirstName());
        assertEquals(originalDate, result.getCreatedDate(), "Debe preservar createdDate del registro existente");

        verify(clientRepository).findById(CLIENT_ID);
        verify(clientRepository).saveClient(updatedInput);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("updateClient: debe lanzar NoDataFoundException cuando no existe cliente por ID")
    void updateClient_throwsNoDataFoundException_whenNotFound() {
        Client updatedInput = Client.builder()
                .id(CLIENT_ID)
                .firstName("New Name")
                .build();

        when(clientRepository.findById(CLIENT_ID)).thenReturn(null);

        assertThrows(NoDataFoundException.class, () -> clientUseCase.updateClient(updatedInput));

        verify(clientRepository).findById(CLIENT_ID);
        verify(clientRepository, never()).saveClient(any());
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("updateClient: debe lanzar NoDataFoundException si el repository devuelve null al guardar")
    void updateClient_throwsNoDataFoundException_whenSaveReturnsNull() {
        LocalDateTime originalDate = LocalDateTime.now().minusDays(1);

        Client existing = Client.builder()
                .id(CLIENT_ID)
                .createdDate(originalDate)
                .build();

        Client updatedInput = existing.toBuilder()
                .firstName("New Name")
                .build();

        when(clientRepository.findById(CLIENT_ID)).thenReturn(existing);
        when(clientRepository.saveClient(updatedInput)).thenReturn(null);

        assertThrows(NoDataFoundException.class, () -> clientUseCase.updateClient(updatedInput));

        verify(clientRepository).findById(CLIENT_ID);
        verify(clientRepository).saveClient(updatedInput);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("getClientById: debe retornar el cliente cuando existe por ID")
    void getClientById_success() {
        Client client = Client.builder().id(CLIENT_ID).firstName("Juan Diego").build();
        when(clientRepository.findById(CLIENT_ID)).thenReturn(client);

        Client found = clientUseCase.getClientById(CLIENT_ID);

        assertEquals(CLIENT_ID, found.getId());
        verify(clientRepository).findById(CLIENT_ID);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("getClientById: debe lanzar NoDataFoundException cuando no existe cliente por ID")
    void getClientById_throwsNoDataFoundException_whenNotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(null);

        assertThrows(NoDataFoundException.class, () -> clientUseCase.getClientById(CLIENT_ID));

        verify(clientRepository).findById(CLIENT_ID);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("findByInfoDocument: debe retornar cliente cuando existe por número y tipo de documento")
    void findByInfoDocument_returnsClient_whenExists() {
        Client client = Client.builder()
                .documentNumber(DOCUMENT_NUMBER)
                .documentType(DOCUMENT_TYPE)
                .build();

        when(clientRepository.findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE))
                .thenReturn(client);

        Client found = clientUseCase.findByInfoDocument(DOCUMENT_NUMBER, DOCUMENT_TYPE);

        assertNotNull(found);
        assertEquals(DOCUMENT_NUMBER, found.getDocumentNumber());
        verify(clientRepository).findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("findByInfoDocument: debe retornar null cuando no existe cliente por documento")
    void findByInfoDocument_returnsNull_whenNotFound() {
        when(clientRepository.findByDocumentNumberAndDocumentType("999", "NIT"))
                .thenReturn(null);

        Client found = clientUseCase.findByInfoDocument("999", "NIT");

        assertNull(found);
        verify(clientRepository).findByDocumentNumberAndDocumentType("999", "NIT");
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("getAll: debe delegar la paginación correctamente al repositorio")
    void getAll_returnsPageResult() {
        int page = 0;
        int size = 10;
        PageResult<Client> pageResult = PageResult.<Client>builder().build();
        when(clientRepository.findAll(page, size)).thenReturn(pageResult);

        PageResult<Client> result = clientUseCase.getAll(size, page);

        assertSame(pageResult, result);
        verify(clientRepository).findAll(page, size);
        verifyNoInteractions(invoiceRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("deleteById: debe eliminar cliente y sus facturas asociadas")
    void deleteById_success() {
        Client client = Client.builder().id(CLIENT_ID).build();
        when(clientRepository.findById(CLIENT_ID)).thenReturn(client);

        clientUseCase.deleteById(CLIENT_ID);

        InOrder inOrder = inOrder(clientRepository, invoiceRepository);
        inOrder.verify(clientRepository).findById(CLIENT_ID);
        inOrder.verify(invoiceRepository).deleteAllByClientId(CLIENT_ID);
        inOrder.verify(clientRepository).deleteClient(CLIENT_ID);

        verifyNoMoreInteractions(clientRepository, invoiceRepository);
    }

    @Test
    @DisplayName("deleteById: debe lanzar NoDataFoundException si no encuentra el cliente al eliminar")
    void deleteById_throwsNoDataFoundException_whenClientNotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(null);

        assertThrows(NoDataFoundException.class, () -> clientUseCase.deleteById(CLIENT_ID));

        verify(clientRepository).findById(CLIENT_ID);
        verifyNoInteractions(invoiceRepository);
        verify(clientRepository, never()).deleteClient(any());
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    @DisplayName("requireNonNull: debe lanzar NoDataFoundException cuando el objeto es null")
    void requireNonNull_throwsNoDataFoundException_whenNull() {
        assertThrows(NoDataFoundException.class, () -> clientUseCase.requireNonNull(null));
        verifyNoInteractions(clientRepository, invoiceRepository);
    }

    @Test
    @DisplayName("requireNonNull: debe retornar el objeto cuando no es null")
    void requireNonNull_returnsObject_whenNotNull() {
        Client client = Client.builder().id(CLIENT_ID).build();
        Client result = clientUseCase.requireNonNull(client);
        assertSame(client, result);
        verifyNoInteractions(clientRepository, invoiceRepository);
    }
}
