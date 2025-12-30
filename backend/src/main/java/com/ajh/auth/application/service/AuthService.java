package com.ajh.auth.application.service;

import com.ajh.auth.application.port.in.AuthUseCase;
import com.ajh.auth.application.port.in.LoginCommand;
import com.ajh.auth.application.port.in.RegisterCommand;
import com.ajh.auth.application.port.out.TokenServicePort;
import com.ajh.auth.adapter.out.persistence.UserJpaRepository; 
import com.ajh.auth.domain.User;
import com.ajh.auth.domain.Role;
import com.ajh.auth.domain.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase { 

    private final UserJpaRepository userRepository; 
    private final PasswordEncoder passwordEncoder;
    private final TokenServicePort tokenServicePort;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginCommand command) {
        // 1. Busca o usuário
        var user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Valida a senha
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Gera o token usando o usuário do banco
        String token = tokenServicePort.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    @Transactional // Garante que a operação de escrita seja atômica
    public AuthResponse register(RegisterCommand command) {
        // 1. Verifica se o e-mail já existe para evitar erro 500 de banco
        if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // 2. Criptografa a senha
        String encodedPassword = passwordEncoder.encode(command.getPassword());
        
        // 3. Cria o objeto User
        User user = User.builder()
        .name(command.getName()) // COMENTE esta linha se o erro persistir
        .email(command.getEmail())
        .password(encodedPassword)
        .role(Role.CANDIDATE) 
        .build();
                
        // 4. Salva e captura o usuário persistido (importante para ter o ID no Token)
        User savedUser = userRepository.save(user);

        // 5. Gera o token usando o usuário que acabou de ser salvo
        String token = tokenServicePort.generateToken(savedUser);
        return new AuthResponse(token);
    }
}