package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class DefaultIdpProvider implements IdpProvider {
    private final TokenProvider tokenProvider;

    public DefaultIdpProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void createClientAccount(String email, String password) {
        // Test implementation - just succeeds
    }

    @Override
    public String authenticateUser(String email, String password) {
        return tokenProvider.createToken(email, "CLIENT");
    }
}
