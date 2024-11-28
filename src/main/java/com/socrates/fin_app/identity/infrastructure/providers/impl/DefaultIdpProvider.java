package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DefaultIdpProvider implements IdpProvider {
    @Override
    public void createClientAccount(String email, String password) {
        // TODO: Implement actual IDP integration
    }

    @Override
    public String authenticateUser(String email, String password) {
        // TODO: Implement actual authentication
        return "dummy.token.fordev";
    }
}
