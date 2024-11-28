package com.socrates.fin_app.identity.infrastructure.security;

public interface TokenProvider {
    String createToken(String username, String role);
    String getUsername(String token);
    String getRole(String token);
}
