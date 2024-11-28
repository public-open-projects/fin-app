package com.socrates.fin_app.identity.application.dto.response;

public record AuthenticationResponse(
    String token,
    String email
) {}
