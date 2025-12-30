// backend/src/main/java/com/ajh/candidate/adapter/out/persistence/JpaCandidateRepository.java
package com.ajh.candidate.adapter.out.persistence;

import com.ajh.candidate.CandidatePersistencePort;
import com.ajh.candidate.domain.Candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// JpaRepository interface base
interface CandidateJpaRepository extends JpaRepository<Candidate, UUID> {
    // Spring Data JPA fornece findById, save, etc.
}

// Adapter que implementa a PORTA de saída (CandidatePersistencePort)
@Repository
public class JpaCandidateRepository implements CandidatePersistencePort {

    private final CandidateJpaRepository repository;

    public JpaCandidateRepository(CandidateJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return repository.save(candidate);
    }

    @Override
    public Optional<Candidate> findById(UUID id) {
        return repository.findById(id);
    }
    
    // Outros métodos de persistência...
}