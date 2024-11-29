package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class DefaultIdpProvider implements IdpProvider {
    private static final String TEST_TOKEN = "test-auth0-token";

    @Override
    public void createClientAccount(String email, String password) {
        // Test implementation - just succeeds
    }

    @Override
    public String authenticateUser(String email, String password) {
        // For test purposes, return a mock Auth0 token
        return TEST_TOKEN;
    }
}
