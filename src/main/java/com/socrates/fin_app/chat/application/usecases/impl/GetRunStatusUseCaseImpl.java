package com.socrates.fin_app.chat.application.usecases.impl;

import com.socrates.fin_app.chat.application.dto.response.RunStatusResponse;
import com.socrates.fin_app.chat.application.usecases.GetRunStatusUseCase;
import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.stereotype.Service;

@Service
public class GetRunStatusUseCaseImpl implements GetRunStatusUseCase {
    private final OpenAIProvider openAIProvider;
    
    public GetRunStatusUseCaseImpl(OpenAIProvider openAIProvider) {
        this.openAIProvider = openAIProvider;
    }
    
    @Override
    public RunStatusResponse execute(String threadId, String runId) {
        RunStatus status = openAIProvider.getRunStatus(threadId, runId);
        return new RunStatusResponse(threadId, runId, status.status().toString(), status.messages());
    }
}
