package com.socrates.fin_app.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatCommandRequest(
    @NotBlank(message = "Session ID is required")
    String sessionId,
    
    @NotBlank(message = "Command is required")
    String command
) {}
