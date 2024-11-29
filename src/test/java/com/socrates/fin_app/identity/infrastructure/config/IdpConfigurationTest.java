package com.socrates.fin_app.identity.infrastructure.config;

import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.providers.impl.Auth0IdpProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IdpConfigurationTest {

    private IdpConfiguration idpConfiguration;

    @BeforeEach
    void setUp() {
        idpConfiguration = new IdpConfiguration();
    }

    @Test
    void whenDevProfile_thenReturnsAuth0IdpProvider() {
        String domain = "test.auth0.com";
        String clientId = "test-client-id";
        String clientSecret = "test-client-secret";
        String connection = "test-connection";
        
        IdpProvider provider = idpConfiguration.auth0IdpProvider(
            domain, clientId, clientSecret, connection);
            
        assertNotNull(provider);
        assertTrue(provider instanceof Auth0IdpProvider);
    }
}
