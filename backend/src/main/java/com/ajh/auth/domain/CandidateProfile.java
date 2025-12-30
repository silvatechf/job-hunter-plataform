package com.ajh.auth.domain; // Certifique-se que o package é este

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "candidate_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    private String seniority;

    @ElementCollection
    private List<String> skills;

    @Column(columnDefinition = "TEXT")
    private String embedding;
}