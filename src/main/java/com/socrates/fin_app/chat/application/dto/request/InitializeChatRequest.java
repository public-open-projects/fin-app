package com.socrates.fin_app.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InitializeChatRequest(
    @NotBlank(message = "User ID is required")
    String userId
) {}
