package com.socrates.fin_app.identity.application.dto.response;

public record ResetPasswordResponse(
    String email,
    String message
) {}
