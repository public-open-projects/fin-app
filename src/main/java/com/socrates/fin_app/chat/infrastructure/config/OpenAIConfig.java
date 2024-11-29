package com.socrates.fin_app.chat.infrastructure.config;

import org.springframework.ai.autoconfigure.openai.OpenAiProperties;
import org.springframework.ai.openai.OpenAiChatClient;
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
    public OpenAiChatClient openAiClient() {
        OpenAiProperties properties = new OpenAiProperties();
        properties.setApiKey(apiKey);
        return new OpenAiChatClient(properties);
    }
}
