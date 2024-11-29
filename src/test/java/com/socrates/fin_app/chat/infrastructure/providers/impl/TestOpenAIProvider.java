package com.socrates.fin_app.chat.infrastructure.providers.impl;

import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class TestOpenAIProvider implements OpenAIProvider {
    @Override
    public String createThread() {
        return "test-thread-id";
    }
    
    @Override
    public String createMessageAndRun(String threadId, String message) {
        return "test-run-id";
    }
    
    @Override
    public RunStatus getRunStatus(String threadId, String runId) {
        return RunStatus.COMPLETED;
    }
}
