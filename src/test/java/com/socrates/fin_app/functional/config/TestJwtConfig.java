package com.socrates.fin_app.functional.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.providers.impl.DefaultIdpProvider;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;

@TestConfiguration
@Profile("test")
public class TestJwtConfig {
    
    @Bean
    public IdpProvider testIdpProvider(TokenProvider tokenProvider) {
        return new DefaultIdpProvider(tokenProvider);
    }
}
