package co.com.management.api.controllers;
import co.com.management.api.Utility;
import co.com.management.api.dto.mappers.ResponseMapper;
import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.api.dto.mappers.RequestMapper;
import co.com.management.usecase.client.ClientUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    @PostMapping(path = "/create")
    public ResponseEntity<?> commandName(@Valid @RequestBody ClientDTO clientDTO) {
        var client = clientUseCase.saveClient(RequestMapper.toModel(clientDTO));
        var response = ResponseMapper.responseFull(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Utility.structureRS(response, HttpStatus.CREATED.value())
        );
    }

    @GetMapping(path = "/all")
    public String hola(){
        return "hola";
    }
}
