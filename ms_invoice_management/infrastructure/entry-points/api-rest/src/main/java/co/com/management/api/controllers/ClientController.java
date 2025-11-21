package co.com.management.api.controllers;
import co.com.management.api.Utility;
import co.com.management.api.dto.mappers.ResponseMapper;
import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.api.dto.mappers.RequestMapper;
import co.com.management.api.dto.models.request.ClientFullDTO;
import co.com.management.usecase.client.ClientUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    @PostMapping(path = "/create")
    public ResponseEntity<?> save(@Valid @RequestBody ClientDTO clientDTO) {
        var client = clientUseCase.saveClient(RequestMapper.toModel(clientDTO));
        var response = ResponseMapper.responseFull(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@Valid @RequestBody ClientFullDTO clientDTO) {
        var client = clientUseCase.updateClient(RequestMapper.toModelFull(clientDTO));
        var response = ResponseMapper.responseFull(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable("id") UUID id) {
        var client = clientUseCase.deleteById(id);
        var response = ResponseMapper.responseFull(client);
        return ResponseEntity.status(HttpStatus.OK).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }


    @GetMapping(path = "{id}")
    public ResponseEntity<?> findByInfoDoc(@PathVariable("id") UUID id){
        var client = clientUseCase.getClientById(id);
        var response = ResponseMapper.response(client);
        return ResponseEntity.status(HttpStatus.FOUND).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAll(@RequestParam("size") int size, @RequestParam("page") int page){
        var client = clientUseCase.getAll(size,page);
        var response = ResponseMapper.toPageResultClientDTO(client);
        return ResponseEntity.status(HttpStatus.FOUND).body(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }
}
