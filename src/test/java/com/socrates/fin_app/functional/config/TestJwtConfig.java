package com.socrates.fin_app.functional.config;

import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import java.util.Date;

@Configuration
public class TestJwtConfig {

    @Bean
    @Primary
    public TokenProvider testTokenProvider() {
        return new TokenProvider() {
            @Override
            public String generateToken(Authentication authentication) {
                return "test-jwt-token";
            }

            @Override
            public String getUsernameFromToken(String token) {
                return "test@example.com";
            }

            @Override
            public Date getExpirationDateFromToken(String token) {
                return new Date(System.currentTimeMillis() + 3600000); // 1 hour
            }

            @Override
            public boolean validateToken(String token) {
                return true; // Always valid for tests
            }
        };
    }
}
