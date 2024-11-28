package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class JwtIdpProvider implements IdpProvider {
    
    @Override
    public void createClientAccount(String email, String password) {
        // For testing, just simulate account creation
        // No actual JWT operations needed
    }
}
