package com.socrates.fin_app.identity.infrastructure.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;

@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.validity}")
    private long validityInMilliseconds;
    
    @Bean
    public Key jwtSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public TokenProvider tokenProvider(Key jwtSigningKey) {
        return new JwtTokenProvider(jwtSigningKey, validityInMilliseconds);
    }
}
