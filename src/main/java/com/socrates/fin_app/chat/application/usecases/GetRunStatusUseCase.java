package com.socrates.fin_app.chat.application.usecases;

import com.socrates.fin_app.chat.application.dto.response.RunStatusResponse;

public interface GetRunStatusUseCase {
    RunStatusResponse execute(String threadId, String runId);
}
