package com.ajh.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a resposta de sucesso para autenticação e registro.
 * Contém o Token JWT que deve ser enviado no cabeçalho Authorization.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    
    private String token;

    // Se no futuro você precisar retornar mais dados (ex: email), 
    // basta adicionar o campo abaixo:
    // private String email;
}