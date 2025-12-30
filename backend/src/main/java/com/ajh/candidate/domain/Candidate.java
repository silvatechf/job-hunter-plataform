// backend/src/main/java/com/ajh/candidate/domain/Candidate.java
package com.ajh.candidate.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

// O @Data do Lombok gera Getters, Setters, construtores, etc.
@Data
@Entity
public class Candidate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private UUID userId; // Chave estrangeira para com.ajh.auth.domain.User

    private String name;
    
    private String cvUrl;
    
    private String currentTitle;
    
    private String seniority;

    // Lista de skills extraídas do CV
    @ElementCollection(fetch = FetchType.EAGER)
    private java.util.List<String> skills;
    
    // Vetor de embedding (string JSON ou binária) usado para matching
    @Column(columnDefinition = "TEXT")
    private String embeddingVector;
}