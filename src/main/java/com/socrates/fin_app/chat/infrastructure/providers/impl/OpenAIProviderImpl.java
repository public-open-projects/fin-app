package com.socrates.fin_app.chat.infrastructure.providers.impl;

import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIProviderImpl implements OpenAIProvider {
    private final OpenAiChatClient openAiClient;
    private final String assistantId;
    
    public OpenAIProviderImpl(
            OpenAiChatClient openAiClient,
            @Value("${openai.assistant.id}") String assistantId) {
        this.openAiClient = openAiClient;
        this.assistantId = assistantId;
    }
    
    @Override
    public String createThread() {
        // TODO: Implement using openAiClient
        return "thread-id"; // Placeholder
    }
    
    @Override
    public String createMessageAndRun(String threadId, String message) {
        // TODO: Implement using openAiClient
        return "run-id"; // Placeholder  
    }
    
    @Override
    public RunStatus getRunStatus(String threadId, String runId) {
        // TODO: Implement using openAiClient
        return null; // Placeholder
    }
}
