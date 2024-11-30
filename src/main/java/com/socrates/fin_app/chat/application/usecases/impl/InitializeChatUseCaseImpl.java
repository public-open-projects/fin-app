package com.socrates.fin_app.chat.application.usecases.impl;

import com.socrates.fin_app.chat.application.dto.request.InitializeChatRequest;
import com.socrates.fin_app.chat.application.dto.response.InitializeChatResponse;
import com.socrates.fin_app.chat.application.usecases.InitializeChatUseCase;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.stereotype.Service;

@Service
public class InitializeChatUseCaseImpl implements InitializeChatUseCase {
    
    private final OpenAIProvider openAIProvider;
    
    public InitializeChatUseCaseImpl(OpenAIProvider openAIProvider) {
        this.openAIProvider = openAIProvider;
    }
    
    @Override
    public InitializeChatResponse execute(InitializeChatRequest request) {
        String threadId = openAIProvider.createThread();
        String welcomeMessage = request.authenticated() 
            ? "Welcome back! How can I assist you today?"
            : "Hello! How can I help you?";
            
        return new InitializeChatResponse(threadId, welcomeMessage);
    }
}
