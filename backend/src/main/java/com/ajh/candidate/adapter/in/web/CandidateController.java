package com.ajh.candidate.adapter.in.web;

import com.ajh.candidate.application.port.in.CandidateProfileDto;
import com.ajh.candidate.application.port.in.CandidateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
@Slf4j
public class CandidateController {

    private final CandidateUseCase candidateUseCase;

    @PostMapping(
    value = "/upload-cv", 
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ResponseEntity<CandidateProfileDto> uploadCv(
    @RequestParam("file") MultipartFile file, // Mudança aqui
    @AuthenticationPrincipal UserDetails userDetails
) {
    log.info("Recebendo arquivo: {} do usuário: {}", file.getOriginalFilename(), userDetails.getUsername());
    return ResponseEntity.ok(candidateUseCase.uploadAndAnalyze(file, userDetails.getUsername()));
}
}