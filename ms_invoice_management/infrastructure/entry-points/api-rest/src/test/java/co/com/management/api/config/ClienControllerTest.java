package co.com.management.api.config;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.NoDataFoundException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.usecase.client.ClientUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienControllerTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private ClientUseCase clientUseCase;

    private final UUID CLIENT_ID = UUID.randomUUID();
    private final String DOCUMENT_NUMBER = "123456789";
    private final String DOCUMENT_TYPE = "CC";

    @Test
    @DisplayName("Debe guardar el cliente exitosamente cuando no existe previamente")
    void saveClient_success() {
        Client client = Client.builder()
                .documentNumber(DOCUMENT_NUMBER)
                .documentType(DOCUMENT_TYPE)
                .firstName("Juan Diego")
                .lastName("Pérez")
                .build();

        when(clientRepository.findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE)).thenReturn(null);
        when(clientRepository.saveClient(any(Client.class))).thenReturn(client.toBuilder().id(CLIENT_ID).build());

        Client saved = clientUseCase.saveClient(client);

        assertNotNull(saved.getId());
        verify(clientRepository).saveClient(client);
    }

    @Test
    @DisplayName("Debe lanzar DataFoundException cuando ya existe un cliente con el mismo documento")
    void saveClient_throwsDataFoundException_whenClientExists() {
        Client existingClient = Client.builder().id(CLIENT_ID).build();
        when(clientRepository.findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE)).thenReturn(existingClient);

        Client newClient = Client.builder()
                .documentNumber(DOCUMENT_NUMBER)
                .documentType(DOCUMENT_TYPE)
                .build();

        assertThrows(DataFoundException.class, () -> clientUseCase.saveClient(newClient));
        verify(clientRepository, never()).saveClient(any());
    }

    @Test
    @DisplayName("Debe actualizar el cliente manteniendo la fecha de creación original")
    void updateClient_success() {
        Client existing = Client.builder()
                .id(CLIENT_ID)
                .firstName("Old Name")
                .createdDate(java.time.LocalDateTime.now().minusDays(1))
                .build();

        Client updatedInput = existing.toBuilder().firstName("New Name").build();

        when(clientRepository.findById(CLIENT_ID)).thenReturn(existing);
        when(clientRepository.saveClient(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Client result = clientUseCase.updateClient(updatedInput);

        assertEquals("New Name", result.getFirstName());
        assertEquals(existing.getCreatedDate(), result.getCreatedDate());
        verify(clientRepository).saveClient(any(Client.class));
    }

    @Test
    @DisplayName("Debe retornar el cliente cuando existe por ID")
    void getClientById_success() {
        Client client = Client.builder().id(CLIENT_ID).firstName("Juan Diego").build();
        when(clientRepository.findById(CLIENT_ID)).thenReturn(client);

        Client found = clientUseCase.getClientById(CLIENT_ID);

        assertEquals(CLIENT_ID, found.getId());
    }

    @Test
    @DisplayName("Debe lanzar NoDataFoundException cuando no existe cliente por ID")
    void getClientById_throwsNoDataFoundException_whenNotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(null);
        assertThrows(NoDataFoundException.class, () -> clientUseCase.getClientById(CLIENT_ID));
    }

    @Test
    @DisplayName("Debe retornar cliente cuando existe por número y tipo de documento")
    void findByInfoDocument_returnsClient_whenExists() {
        Client client = Client.builder().documentNumber(DOCUMENT_NUMBER).build();
        when(clientRepository.findByDocumentNumberAndDocumentType(DOCUMENT_NUMBER, DOCUMENT_TYPE)).thenReturn(client);

        Client found = clientUseCase.findByInfoDocument(DOCUMENT_NUMBER, DOCUMENT_TYPE);

        assertEquals(DOCUMENT_NUMBER, found.getDocumentNumber());
    }

    @Test
    @DisplayName("Debe retornar null cuando no existe cliente por documento")
    void findByInfoDocument_returnsNull_whenNotFound() {
        when(clientRepository.findByDocumentNumberAndDocumentType(anyString(), anyString())).thenReturn(null);

        Client found = clientUseCase.findByInfoDocument("999", "NIT");

        assertNull(found);
    }

    @Test
    @DisplayName("Debe delegar la paginación correctamente al repositorio")
    void getAll_returnsPageResult() {
        PageResult<Client> pageResult = PageResult.<Client>builder().build();
        when(clientRepository.findAll(0, 10)).thenReturn(pageResult);

        PageResult<Client> result = clientUseCase.getAll(10, 0);

        assertEquals(pageResult, result);
    }

    @Test
    @DisplayName("Debe eliminar cliente y factura cuando tiene factura asociada")
    void deleteById_success_withInvoice() {
        Client client = Client.builder().id(CLIENT_ID).build();
        Invoice invoice = Invoice.builder().code("INV-001").clientId(CLIENT_ID.toString()).build();

        when(invoiceRepository.findByClientId(CLIENT_ID.toString())).thenReturn(invoice);
        when(invoiceRepository.deleteInvoice("INV-001")).thenReturn(invoice);
        when(clientRepository.deleteClient(CLIENT_ID)).thenReturn(client);

        Client deleted = clientUseCase.deleteById(CLIENT_ID);

        assertEquals(CLIENT_ID, deleted.getId());
        verify(invoiceRepository).deleteInvoice("INV-001");
        verify(clientRepository).deleteClient(CLIENT_ID);
    }

    @Test
    @DisplayName("Debe eliminar solo el cliente cuando no tiene factura asociada")
    void deleteById_success_withoutInvoice() {
        Client client = Client.builder().id(CLIENT_ID).build();

        when(invoiceRepository.findByClientId(CLIENT_ID.toString())).thenReturn(null);
        when(clientRepository.deleteClient(CLIENT_ID)).thenReturn(client);

        Client deleted = clientUseCase.deleteById(CLIENT_ID);

        assertEquals(CLIENT_ID, deleted.getId());
        verify(invoiceRepository, never()).deleteInvoice(any());
    }

    @Test
    @DisplayName("Debe lanzar NoDataFoundException cuando no se elimina ni cliente ni factura")
    void deleteById_throwsNoDataFoundException_whenNothingDeleted() {
        when(invoiceRepository.findByClientId(anyString())).thenReturn(null);
        when(clientRepository.deleteClient(CLIENT_ID)).thenReturn(null);

        assertThrows(NoDataFoundException.class, () -> clientUseCase.deleteById(CLIENT_ID));
    }

    @Test
    @DisplayName("requireNonNull debe lanzar NoDataFoundException cuando el objeto es null")
    void requireNonNull_throwsNoDataFoundException_whenNull() {
        assertThrows(NoDataFoundException.class, () -> clientUseCase.requireNonNull(null));
    }

    @Test
    @DisplayName("requireNonNull debe retornar el objeto cuando no es null")
    void requireNonNull_returnsObject_whenNotNull() {
        Client client = Client.builder().id(CLIENT_ID).build();
        Client result = clientUseCase.requireNonNull(client);
        assertEquals(client, result);
    }
}
