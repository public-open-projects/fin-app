package com.socrates.fin_app.chat.infrastructure.providers.impl;

import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class OpenAIProviderImpl implements OpenAIProvider {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1";
    
    private final RestTemplate restTemplate;
    private final String assistantId;
    
    public OpenAIProviderImpl(
            RestTemplate openAiRestTemplate,
            @Value("${openai.assistant.id}") String assistantId) {
        this.restTemplate = openAiRestTemplate;
        this.assistantId = assistantId;
    }
    
    @Override
    public String createThread() {
        String url = OPENAI_API_URL + "/threads";
        var response = restTemplate.postForObject(url, new HttpEntity<>(Map.of()), Map.class);
        return (String) response.get("id");
    }
    
    @Override
    public String createMessageAndRun(String threadId, String message) {
        // First create message
        String messageUrl = OPENAI_API_URL + "/threads/" + threadId + "/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, Object> messageBody = Map.of(
            "role", "user",
            "content", message
        );
        
        restTemplate.postForObject(messageUrl, new HttpEntity<>(messageBody, headers), Map.class);
        
        // Then create run
        String runUrl = OPENAI_API_URL + "/threads/" + threadId + "/runs";
        Map<String, Object> runBody = Map.of(
            "assistant_id", assistantId
        );
        
        var runResponse = restTemplate.postForObject(runUrl, new HttpEntity<>(runBody, headers), Map.class);
        return (String) runResponse.get("id");
    }
    
    @Override
    public RunStatus getRunStatus(String threadId, String runId) {
        String url = OPENAI_API_URL + "/threads/" + threadId + "/runs/" + runId;
        var response = restTemplate.getForObject(url, Map.class);
        
        String status = (String) response.get("status");
        // Convert OpenAI status to our RunStatus enum
        // TODO: Implement proper status mapping
        return null;
    }
}
