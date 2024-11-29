package com.socrates.fin_app.chat.application.dto.response;

public record ChatCommandResponse(
    String sessionId,
    String result,
    String nextStep
) {}
