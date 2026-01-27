package co.com.management.api.controllers;

import co.com.management.api.Utility;
import co.com.management.api.controllers.docs.CorrectExample;
import co.com.management.api.controllers.docs.ErrorExample;
import co.com.management.api.dto.mappers.RequestMapper;
import co.com.management.api.dto.mappers.ResponseMapper;
import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.usecase.client.ClientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/clients", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class ClientController {

    private final ClientUseCase clientUseCase;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    @Operation(
            summary = "Crear cliente",
            description = "Crea un nuevo cliente en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Registrar cliente",
                                    summary = "Crear nuevo cliente",
                                    value = CorrectExample.CLIENT_CREATE
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Cliente registrado previamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Cliente ya registrado",
                                    summary = "Error de cliente duplicado",
                                    value = ErrorExample.CLIENT_ALREADY_EXISTS
                            )
                    )
            )
    })
    @PostMapping(path = "/create")
    public ResponseEntity<?> save(@Valid @RequestBody ClientDTO clientDTO) {
        var client = clientUseCase.saveClient(requestMapper.toModel(clientDTO));
        var response = responseMapper.responseFull(client);
        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@Validated(ClientDTO.Update.class) @RequestBody ClientDTO clientDTO) {
        var client = clientUseCase.updateClient(requestMapper.toModel(clientDTO));
        var response = responseMapper.responseFull(client);
        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable("id") String id) {
        clientUseCase.deleteById(id);
        return ResponseEntity.ok(
                Utility.structureRS("", HttpStatus.OK.value())
        );
    }


    @GetMapping(path = "{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id){
        var client = clientUseCase.getClientById(id);
        var response = responseMapper.response(client);
        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        var client = clientUseCase.getAll(size, page);
        var response = responseMapper.toPageResultClientDTO(client);

        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> getByDocument(
            @RequestParam("documentNumber") String documentNumber,
            @RequestParam("documentType") String documentType
    ) {
        var client = clientUseCase.searchByInfo(documentNumber, documentType);
        var response = responseMapper.responseFull(client);

        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }
}
