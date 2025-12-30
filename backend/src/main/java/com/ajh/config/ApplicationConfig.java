// backend/src/main/java/com/ajh/config/ApplicationConfig.java
package com.ajh.config;

import com.ajh.auth.application.port.out.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserPersistencePort userPersistencePort;

    // 1. UserDetailsService: Como o Spring encontra o usuário pelo email
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userPersistencePort.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // 2. AuthenticationProvider: Mecanismo que verifica o usuário e a senha
    @Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService()); // Busca no banco
    authProvider.setPasswordEncoder(passwordEncoder()); // USA O MESMO ENCODER DO REGISTRO
    return authProvider;
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Certifique-se de que é o mesmo usado no AuthService
}
    
    // O MÉTODO restTemplate() FOI REMOVIDO DAQUI 
    // PARA EVITAR CONFLITO COM IntegrationConfig.java
}