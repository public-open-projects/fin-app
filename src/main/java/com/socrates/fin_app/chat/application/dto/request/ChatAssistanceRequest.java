package com.socrates.fin_app.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatAssistanceRequest(
    @NotBlank(message = "Session ID is required")
    String sessionId,
    
    @NotBlank(message = "Message is required")
    String message
) {}
