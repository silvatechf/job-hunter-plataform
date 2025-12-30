// backend/src/main/java/com/ajh/candidate/application/port/out/AiAnalysisPort.java
package com.ajh.candidate;

import com.ajh.candidate.domain.AiAnalysisResult;
import org.springframework.web.multipart.MultipartFile;
import com.ajh.candidate.domain.Candidate; // Adicionado para referência, se necessário

// Porta de Saída para o Serviço de Análise de IA (Adapter Python)
public interface AiAnalysisPort {

    // CORREÇÃO: Ajuste a assinatura do método para aceitar String (URL) e MultipartFile
    AiAnalysisResult analyzeCv(String cvUrl, MultipartFile cvFile);
}