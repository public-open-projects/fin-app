package com.socrates.fin_app.chat.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

@Configuration
public class OpenAIConfig {
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.assistant.id}")
    private String assistantId;
    
    @PostConstruct
    public void validateAssistantId() {
        // TODO: Implement assistant validation on startup
    }
    
    @Bean
    @ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = true)
    public RestTemplate openAiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(apiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
