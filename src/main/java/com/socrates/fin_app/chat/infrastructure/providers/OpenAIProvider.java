package com.socrates.fin_app.chat.infrastructure.providers;

import com.socrates.fin_app.chat.domain.entities.RunStatus;

public interface OpenAIProvider {
    String createThread();
    String createMessageAndRun(String threadId, String message);
    RunStatus getRunStatus(String threadId, String runId);
}
