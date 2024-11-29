package com.socrates.fin_app.chat.application.dto.response;

public record InitializeChatResponse(
    String sessionId,
    String welcomeMessage
) {}
