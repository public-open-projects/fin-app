package com.socrates.fin_app.functional.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.providers.impl.DefaultIdpProvider;

@TestConfiguration
@Profile("test")
public class TestJwtConfig {
    
    @Bean
    public IdpProvider testIdpProvider() {
        return new DefaultIdpProvider();
    }
}
