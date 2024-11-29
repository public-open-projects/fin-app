package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestJwtConfig {
    private static final String TEST_SECRET = "test-secret-key-that-is-long-enough-for-testing-purposes-and-algorithms";
    private static final long VALIDITY = 3600000L; // 1 hour

    @Bean
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider(
            Keys.hmacShaKeyFor(TEST_SECRET.getBytes()),
            VALIDITY
        );
    }
}
