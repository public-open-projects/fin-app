package com.socrates.fin_app.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateMessageRequest(
    @NotBlank(message = "Message content is required")
    String message
) {}
