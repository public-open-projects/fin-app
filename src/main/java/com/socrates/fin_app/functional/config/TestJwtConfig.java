package com.socrates.fin_app.functional.config;

import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.security.Key;

@Configuration
@Profile("test")
public class TestJwtConfig {
    
    @Bean
    public Key jwtKey() {
        // Use a fixed key for testing
        String defaultSecret = "defaultTestSecretKeyThatIsLongEnoughForHS256Algorithm";
        return Keys.hmacShaKeyFor(defaultSecret.getBytes());
    }

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider(Key jwtKey) {
        return new JwtTokenProvider(jwtKey, 3600000); // 1 hour validity
    }
}
