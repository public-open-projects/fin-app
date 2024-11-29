package com.socrates.fin_app.functional.config;

import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestJwtConfig {
    private static final String TEST_SECRET = "test-secret-key-that-is-long-enough-for-testing-purposes-and-algorithms";
    private static final long VALIDITY = 3600000L; // 1 hour

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(
            Keys.hmacShaKeyFor(TEST_SECRET.getBytes()),
            VALIDITY
        );
    }
}
