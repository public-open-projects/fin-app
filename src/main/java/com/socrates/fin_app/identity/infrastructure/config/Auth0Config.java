package com.socrates.fin_app.identity.infrastructure.config;

import com.auth0.client.auth.AuthAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class Auth0Config {
    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Bean
    public AuthAPI auth0Client() {
        return new AuthAPI(domain, clientId, clientSecret);
    }
}
package com.socrates.fin_app.identity.infrastructure.config;

import com.auth0.client.auth.AuthAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Auth0Config {
    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Bean
    public AuthAPI auth0Client() {
        return new AuthAPI(domain, clientId, clientSecret);
    }
}
