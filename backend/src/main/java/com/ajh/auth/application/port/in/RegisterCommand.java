package com.ajh.auth.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // ESSENCIAL: Permite que o Spring crie o objeto antes de preencher os dados
@AllArgsConstructor // Permite criar o objeto com todos os dados de uma vez
public class RegisterCommand {
    private String email;
    private String password;
    private String name;
}