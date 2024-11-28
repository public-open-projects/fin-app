package com.socrates.fin_app.identity.application.dto.response;

public record ProfileResponse(
    String id,
    String email,
    String firstName,
    String lastName,
    String phoneNumber
) {}
