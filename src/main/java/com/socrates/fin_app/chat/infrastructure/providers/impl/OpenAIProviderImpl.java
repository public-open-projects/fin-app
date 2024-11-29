package com.socrates.fin_app.chat.infrastructure.providers.impl;

import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import com.socrates.fin_app.chat.infrastructure.clients.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIProviderImpl implements OpenAIProvider {
    private final OpenAIClient openAIClient;
    private final String assistantId;
    
    public OpenAIProviderImpl(
            OpenAIClient openAIClient,
            @Value("${openai.assistant.id}") String assistantId) {
        this.openAIClient = openAIClient;
        this.assistantId = assistantId;
    }
    
    @Override
    public String createThread() {
        return openAIClient.createThread();
    }
    
    @Override
    public String createMessageAndRun(String threadId, String message) {
        openAIClient.createMessage(threadId, message);
        return openAIClient.createRun(threadId, assistantId);
    }
    
    @Override
    public RunStatus getRunStatus(String threadId, String runId) {
        return openAIClient.getRunStatus(threadId, runId);
    }
}
