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
@RequestMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    @PostMapping(path = "/create")
    public ResponseEntity<?> save(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        var client = invoiceUseCase.saveInvoice(RequestMapper.toModel(invoiceDTO), invoiceDTO.getClientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Utility.structureRS(client, HttpStatus.OK.value())
        );
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAll(@RequestParam(name = "size", defaultValue = "1") int size,
                                    @RequestParam(name = "page", defaultValue = "0") int page){
        var client = invoiceUseCase.getAllInvoices(page,size);
        var response = ResponseMapper.toPageResultInvoiceDTO(client);
        return ResponseEntity.status(HttpStatus.FOUND).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping(path = "/allByClient/{clientId}")
    public ResponseEntity<?> getAllByClient(@PathVariable("clientId") String clientId,
                                            @RequestParam(name = "size", defaultValue = "1") int size,
                                            @RequestParam(name = "page", defaultValue = "0") int page){
        var client = invoiceUseCase.getAllByClientId(clientId, page, size);
        var response = ResponseMapper.toPageResultInvoiceDTO(client);
        return ResponseEntity.status(HttpStatus.FOUND).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping(path = "{invoiceId}")
    public ResponseEntity<?> getById(@PathVariable("invoiceId") String invoiceId){
        var invoice = invoiceUseCase.getById(invoiceId);
        var response = ResponseMapper.response(invoice);
        return ResponseEntity.status(HttpStatus.FOUND).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );

    }

    @DeleteMapping(path = "{invoiceId}")
    public ResponseEntity<?> deleteById(@PathVariable("invoiceId") String invoiceId){
        invoiceUseCase.deleteById(invoiceId);
        return ResponseEntity.status(HttpStatus.OK).body(
                Utility.structureRS("", HttpStatus.OK.value())
        );
    }

    }

