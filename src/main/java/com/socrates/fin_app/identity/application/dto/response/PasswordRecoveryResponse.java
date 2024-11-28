package com.socrates.fin_app.identity.application.dto.response;

public record PasswordRecoveryResponse(
    String email,
    String message
) {}
