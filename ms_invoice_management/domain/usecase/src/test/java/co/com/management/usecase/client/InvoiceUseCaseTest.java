package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.calculatedinvoice.CalculatedInvoice;
import co.com.management.model.calculatedinvoice.gateways.CalculatedInvoiceRepository;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static co.com.management.usecase.client.InvoiceTestData.CLIENT_ID;
import static co.com.management.usecase.client.InvoiceTestData.aClient;
import static co.com.management.usecase.client.InvoiceTestData.anInvoiceInput;
import static co.com.management.usecase.client.InvoiceTestData.anInvoiceWithTotal;
import static co.com.management.usecase.client.InvoiceTestData.aPageResultInvoices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceUseCaseTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ClientUseCase clientUseCase;

    @Mock
    private CalculatedInvoiceRepository calculatedInvoiceRepository;

    @InjectMocks
    private InvoiceUseCase invoiceUseCase;

    @Test
    @DisplayName("saveInvoice: debe generar id, calcular total y registrar la factura asociada al cliente")
    void saveInvoice_success_withoutCaptors() {
        String clientId = CLIENT_ID;
        Client client = aClient(clientId);

        Invoice invoiceInput = anInvoiceInput(clientId); // sin id, sin totalAmount
        Invoice calculatedResult = anInvoiceWithTotal(clientId, BigDecimal.valueOf(150_000.0));
        Invoice registeredResult = anInvoiceWithTotal(clientId, BigDecimal.valueOf(150_000.0));

        when(clientUseCase.getClientById(clientId)).thenReturn(client);

        when(calculatedInvoiceRepository.calculateInvoice(any(CalculatedInvoice.class)))
                .thenReturn(calculatedResult);

        when(invoiceRepository.registerInvoice(any(Invoice.class), eq(client)))
                .thenReturn(registeredResult);

        Invoice result = invoiceUseCase.saveInvoice(invoiceInput, clientId);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150_000.0), result.getTotalAmount());

        assertNotNull(invoiceInput.getId(), "Debe setearse un id en la factura");
        assertFalse(invoiceInput.getId().isBlank(), "El id generado no debe estar vacío");

        verify(clientUseCase).getClientById(clientId);
        verify(calculatedInvoiceRepository).calculateInvoice(any(CalculatedInvoice.class));
        verify(invoiceRepository).registerInvoice(any(Invoice.class), eq(client));

        verifyNoMoreInteractions(clientUseCase, calculatedInvoiceRepository, invoiceRepository);
    }


    @Test
    @DisplayName("saveInvoice: debe propagar excepción si getClientById falla y no llamar repositorios")
    void saveInvoice_shouldNotCallRepositories_whenClientNotFound() {
        // Arrange
        String clientId = CLIENT_ID;
        Invoice invoiceInput = anInvoiceInput(clientId);

        when(clientUseCase.getClientById(clientId)).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> invoiceUseCase.saveInvoice(invoiceInput, clientId));
        assertEquals("boom", ex.getMessage());

        verify(clientUseCase).getClientById(clientId);
        verifyNoInteractions(calculatedInvoiceRepository, invoiceRepository);
        verifyNoMoreInteractions(clientUseCase);
    }

    @Test
    @DisplayName("getAllByClientId: debe delegar al repositorio con page y size")
    void getAllByClientId_shouldDelegate() {
        int page = 1;
        int size = 10;

        PageResult<Invoice> expected = aPageResultInvoices();
        when(invoiceRepository.getAllByClientId(CLIENT_ID, page, size)).thenReturn(expected);

        PageResult<Invoice> result = invoiceUseCase.getAllByClientId(CLIENT_ID, page, size);

        assertSame(expected, result);
        verify(invoiceRepository).getAllByClientId(CLIENT_ID, page, size);
        verifyNoInteractions(clientUseCase, calculatedInvoiceRepository);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    @DisplayName("getAllInvoices: debe delegar al repositorio pageable")
    void getAllInvoices_shouldDelegate() {
        int page = 0;
        int size = 20;

        PageResult<Invoice> expected = aPageResultInvoices();
        when(invoiceRepository.getAllPageable(page, size)).thenReturn(expected);

        PageResult<Invoice> result = invoiceUseCase.getAllInvoices(page, size);

        assertSame(expected, result);
        verify(invoiceRepository).getAllPageable(page, size);
        verifyNoInteractions(clientUseCase, calculatedInvoiceRepository);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    @DisplayName("deleteById: debe delegar deleteInvoice al repositorio")
    void deleteById_shouldDelegate() {
        String invoiceId = "inv-1";

        invoiceUseCase.deleteById(invoiceId);

        verify(invoiceRepository).deleteInvoice(invoiceId);
        verifyNoInteractions(clientUseCase, calculatedInvoiceRepository);
        verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    @DisplayName("getById: debe delegar getById al repositorio")
    void getById_shouldDelegate() {
        String invoiceId = "inv-1";
        Invoice expected = anInvoiceWithTotal(CLIENT_ID, BigDecimal.valueOf(99.0));
        when(invoiceRepository.getById(invoiceId)).thenReturn(expected);

        Invoice result = invoiceUseCase.getById(invoiceId);

        assertSame(expected, result);
        verify(invoiceRepository).getById(invoiceId);
        verifyNoInteractions(clientUseCase, calculatedInvoiceRepository);
        verifyNoMoreInteractions(invoiceRepository);
    }
}
