package com.ajh.auth.application.port.in.web;

import com.ajh.auth.application.port.in.AuthUseCase;
import com.ajh.auth.application.port.in.LoginCommand;
import com.ajh.auth.application.port.in.RegisterCommand;
import com.ajh.auth.domain.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor // Isso gera o construtor que o Spring usa para injetar dependências
public class AuthController {

    // REMOVIDO o "= null". O Spring cuidará da injeção via construtor.
    private final AuthUseCase authUseCase;

    @PostMapping(
        value = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterCommand command) {
        // Agora o authUseCase não será mais null
        return ResponseEntity.ok(authUseCase.register(command));
    }

    @PostMapping(
        value = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginCommand command) {
        return ResponseEntity.ok(authUseCase.login(command));
    }
}