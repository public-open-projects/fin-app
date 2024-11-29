package com.socrates.fin_app.identity.infrastructure.config;

import com.auth0.client.auth.AuthAPI;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.providers.impl.Auth0IdpProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class IdpConfiguration {

    @Bean
    @Profile({"dev", "prod"})
    public IdpProvider auth0IdpProvider(
            AuthAPI auth0Client,
            @Value("${auth0.connection}") String connection) {
        return new Auth0IdpProvider(auth0Client, connection);
    }
}
