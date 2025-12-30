// backend/src/main/java/com/ajh/candidate/domain/AiAnalysisResult.java
package com.ajh.candidate.domain;

import lombok.Data;
import java.util.List;

// DTO para receber a resposta do AI Engine (Python)
@Data
public class AiAnalysisResult {
    private String currentTitle;
    private String seniority;
    private List<String> skills;
    // O campo de embedding para o vetor
    private String embedding; 
}