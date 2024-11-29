package com.socrates.fin_app.chat.infrastructure.config;

import com.socrates.fin_app.chat.infrastructure.clients.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public OpenAIClient openAIClient() {
        return OpenAIClient.builder()
            .apiKey(apiKey)
            .build();
    }
}
