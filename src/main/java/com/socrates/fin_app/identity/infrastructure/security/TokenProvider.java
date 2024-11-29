package com.socrates.fin_app.identity.infrastructure.security;

public interface TokenProvider {
    String createToken(String subject, String role);
    String getSubjectFromToken(String token);
    boolean validateToken(String token);
}
