// backend/src/main/java/com/ajh/candidate/adapter/out/ai/AiEngineClient.java
package com.ajh.candidate.adapter.out.ai;

import com.ajh.candidate.AiAnalysisPort;
import com.ajh.candidate.domain.AiAnalysisResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AiEngineClient implements AiAnalysisPort {

    private final RestTemplate restTemplate;

    // CORREÇÃO: Adicionado o prefixo "ajh." para bater com o application.yml
    @Value("${ajh.ai-engine.url}")
    private String aiEngineUrl;

    @Override
    public AiAnalysisResult analyzeCv(String cvUrl, MultipartFile cvFile) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            ByteArrayResource resource = new ByteArrayResource(cvFile.getBytes()) {
                @Override
                public String getFilename() {
                    return cvFile.getOriginalFilename();
                }
            };
            
            body.add("file", resource);
            body.add("cv_url", cvUrl); 
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Monta a URL final
            String apiUrl = aiEngineUrl + "/analyze";
            
            return restTemplate.postForObject(
                apiUrl, 
                requestEntity, 
                AiAnalysisResult.class
            );

        } catch (IOException e) {
            throw new RuntimeException("Error processing CV file for AI analysis.", e);
        }
    }
}