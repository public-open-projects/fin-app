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
    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(
            Keys.hmacShaKeyFor(TestConstants.JWT_SECRET_KEY.getBytes()),
            TestConstants.JWT_VALIDITY
        );
    }
}
