package com.socrates.fin_app.functional.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.providers.impl.DefaultIdpProvider;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

@TestConfiguration
@Profile("test")
public class TestJwtConfig {
    
    private static final String SECRET_KEY = "test-secret-key-long-enough-for-jwt-signing";
    private static final long TOKEN_VALIDITY = 3600000; // 1 hour
    
    @Bean
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider(
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)),
            TOKEN_VALIDITY
        );
    }
    
    @Bean
    public IdpProvider testIdpProvider(TokenProvider tokenProvider) {
        return new DefaultIdpProvider(tokenProvider);
    }
}
