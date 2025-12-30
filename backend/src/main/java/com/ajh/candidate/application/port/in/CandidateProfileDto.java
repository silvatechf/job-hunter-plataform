package com.ajh.candidate.application.port.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDto {

    @JsonProperty("extracted_text")
    private String extractedText;

    @JsonProperty("skills")
    private List<String> skills;

    @JsonProperty("seniority")
    private String seniority;

    @JsonProperty("embedding")
    private List<Double> embedding;
}