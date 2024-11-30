package com.socrates.fin_app.chat.infrastructure.providers.impl;

import org.springframework.context.annotation.Profile;
import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;
import java.util.Collections;

@Service
@Profile("!test")
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
        RunStatus.Status mappedStatus = mapOpenAIStatus(status);
        List<String> messages = Collections.emptyList(); // TODO: Implement message extraction
        
        return new RunStatus(mappedStatus, messages);
    }

    private RunStatus.Status mapOpenAIStatus(String openAIStatus) {
        return switch (openAIStatus) {
            case "completed" -> RunStatus.Status.COMPLETED;
            case "in_progress" -> RunStatus.Status.IN_PROGRESS;
            case "failed" -> RunStatus.Status.FAILED;
            case "cancelled" -> RunStatus.Status.CANCELLED;
            case "expired" -> RunStatus.Status.EXPIRED;
            default -> RunStatus.Status.QUEUED;
        };
    }
}
