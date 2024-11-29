package com.socrates.fin_app.functional.config;

import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestJwtConfig {
    
    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        // Using a fixed secret for testing purposes
        return new JwtTokenProvider(
            "testSecretKeyThatIsLongEnoughForHS256Algorithm12345",
            3600000 // 1 hour
        );
    }
}
