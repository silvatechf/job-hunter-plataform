package com.ajh.candidate.application.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface CandidateUseCase {
    // Sincronizado com o Controller: recebe File e Email (String)
    CandidateProfileDto uploadAndAnalyze(MultipartFile file, String email);
}