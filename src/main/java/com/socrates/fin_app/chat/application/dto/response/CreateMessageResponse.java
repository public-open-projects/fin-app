package com.socrates.fin_app.chat.application.dto.response;

public record CreateMessageResponse(
    String threadId,
    String runId,
    String status
) {}
