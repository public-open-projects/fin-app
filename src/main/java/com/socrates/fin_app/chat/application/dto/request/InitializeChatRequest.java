package com.socrates.fin_app.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InitializeChatRequest(
    @NotBlank(message = "Session identifier is required")
    String identifier,
    
    boolean authenticated
) {}
