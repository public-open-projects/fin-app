package com.socrates.fin_app.chat.application.usecases.impl;

import com.socrates.fin_app.chat.application.dto.request.CreateMessageRequest;
import com.socrates.fin_app.chat.application.dto.response.CreateMessageResponse;
import com.socrates.fin_app.chat.application.usecases.CreateMessageUseCase;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.stereotype.Service;

@Service
public class CreateMessageUseCaseImpl implements CreateMessageUseCase {
    private final OpenAIProvider openAIProvider;
    
    public CreateMessageUseCaseImpl(OpenAIProvider openAIProvider) {
        this.openAIProvider = openAIProvider;
    }
    
    @Override
    public CreateMessageResponse execute(String threadId, CreateMessageRequest request) {
        String runId = openAIProvider.createMessageAndRun(threadId, request.message());
        return new CreateMessageResponse(threadId, runId, "started");
    }
}
