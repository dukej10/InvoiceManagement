package co.com.management.api.controllers;


import co.com.management.api.Utility;
import co.com.management.api.dto.mappers.RequestMapper;
import co.com.management.api.dto.mappers.ResponseMapper;
import co.com.management.api.dto.models.request.InvoiceDTO;
import co.com.management.usecase.client.InvoiceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    @PostMapping(path = "/create")
    public ResponseEntity<?> save(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        var client = invoiceUseCase.saveInvoice(requestMapper.toModel(invoiceDTO), invoiceDTO.getClientId());
        return ResponseEntity.ok(
                Utility.structureRS(client, HttpStatus.OK.value())
        );
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAll(@RequestParam(name = "size", defaultValue = "1") int size,
                                    @RequestParam(name = "page", defaultValue = "0") int page){
        var client = invoiceUseCase.getAllInvoices(page,size);
        var response = responseMapper.toPageResultInvoiceDTO(client);
        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getInvoicesByClient(
            @PathVariable("clientId") String clientId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        var invoices = invoiceUseCase.getAllByClientId(clientId, page, size);
        var response = responseMapper.toPageResultInvoiceDTO(invoices);

        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String invoiceId){
        var invoice = invoiceUseCase.getById(invoiceId);
        var response = responseMapper.response(invoice);
        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );

    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String invoiceId){
        invoiceUseCase.deleteById(invoiceId);
        return ResponseEntity.ok(
                Utility.structureRS("", HttpStatus.OK.value())
        );
    }

    }

