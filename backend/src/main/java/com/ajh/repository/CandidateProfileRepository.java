package com.ajh.repository;

import com.ajh.auth.domain.CandidateProfile; // Corrigido de com.ajh.domain para com.ajh.auth.domain
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, UUID> {
}