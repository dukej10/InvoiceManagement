package co.com.management.api.controllers;

import co.com.management.api.Utility;
import co.com.management.api.dto.mappers.RequestMapper;
import co.com.management.api.dto.mappers.ResponseMapper;
import co.com.management.api.dto.models.request.ClientDTO;
import co.com.management.api.dto.models.request.LoginDTO;
import co.com.management.security.JwtProvider;
import co.com.management.usecase.login.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final JwtProvider jwtProvider;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;


    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        var user = loginUseCase.login(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );
        var token = jwtProvider.generate(user.getUsername(), user.getRoles());
        var response = responseMapper.toLoginRSDTO(token);
        return ResponseEntity.ok(
                Utility.structureRS(response, HttpStatus.OK.value())
        );
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> save(@Valid @RequestBody LoginDTO loginDTO) {
        var user = loginUseCase.register(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Utility.structureRS("Usuario registrado", HttpStatus.CREATED.value())
        );
    }
}
