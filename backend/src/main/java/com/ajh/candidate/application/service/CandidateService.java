package com.ajh.candidate.application.service;

import com.ajh.auth.domain.User;
import com.ajh.auth.adapter.out.persistence.UserJpaRepository;
import com.ajh.candidate.application.port.in.CandidateUseCase;
import com.ajh.candidate.application.port.in.CandidateProfileDto;
import com.ajh.auth.domain.CandidateProfile; 
import com.ajh.repository.CandidateProfileRepository; 

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService implements CandidateUseCase {

    private final RestTemplate restTemplate;
    private final CandidateProfileRepository repository;
    private final UserJpaRepository userRepository; // Adicionado para buscar o User pelo email

    @Value("${ajh.ai-engine.url:http://ai-engine:8000/api/v1/analyze-cv}")
    private String aiEngineUrl;

    @Override
    @Transactional
    public CandidateProfileDto uploadAndAnalyze(MultipartFile file, String email) {
        log.info("Iniciando processamento para o email: {}", email);

        // 1. Busca o usuário no banco (necessário para pegar o UUID)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));

        CandidateProfileDto dto = createEmptyDto();

        try {
            // 2. Prepara a chamada para o AI-ENGINE (Python)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            ResponseEntity<CandidateProfileDto> response = restTemplate.postForEntity(
                    aiEngineUrl,
                    requestEntity,
                    CandidateProfileDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                dto = response.getBody();
                log.info("IA processou o currículo com sucesso");
            }

        } catch (Exception e) {
            log.error("Falha na comunicação com a IA: {}. Usando dados padrão.", e.getMessage());
            dto.setExtractedText("Erro no processamento da IA");
        }

        // 3. Persiste o perfil do candidato vinculado ao ID do usuário
        CandidateProfile profile = CandidateProfile.builder()
                .userId(user.getId())
                .extractedText(dto.getExtractedText())
                .seniority(dto.getSeniority() != null ? dto.getSeniority() : "PENDING")
                .skills(dto.getSkills() != null ? dto.getSkills() : Collections.emptyList())
                .build();

        repository.save(profile);
        log.info("Perfil salvo no banco para o usuário ID: {}", user.getId());

        return dto;
    }

    private CandidateProfileDto createEmptyDto() {
        return CandidateProfileDto.builder()
                .skills(Collections.emptyList())
                .seniority("UNKNOWN")
                .extractedText("")
                .build();
    }
}