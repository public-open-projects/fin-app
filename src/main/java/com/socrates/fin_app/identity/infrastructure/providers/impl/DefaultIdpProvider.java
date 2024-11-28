package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.stereotype.Component;

@Component
public class DefaultIdpProvider implements IdpProvider {
    @Override
    public void createClientAccount(String email, String password) {
        // TODO: Implement actual IDP integration
    }
}
