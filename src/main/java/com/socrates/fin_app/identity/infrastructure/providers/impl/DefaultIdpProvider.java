package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

@Component
@Profile("test")
public class DefaultIdpProvider implements IdpProvider {
    private static final String SECRET = "test-secret-key-long-enough-for-jwt-signing";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    @Override
    public void createClientAccount(String email, String password) {
        // Test implementation - just succeeds
    }

    @Override
    public String authenticateUser(String email, String password) {
        // For test purposes, always authenticate and return a JWT token
        return createToken(email);
    }

    private String createToken(String email) {
        return JWT.create()
            .withSubject(email)
            .withClaim("role", "CLIENT")
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(Algorithm.HMAC256(SECRET));
    }
}
