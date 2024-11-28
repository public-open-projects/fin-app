package com.socrates.fin_app.identity.infrastructure.providers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DefaultIdpProviderTest {

    private DefaultIdpProvider idpProvider;

    @BeforeEach
    void setUp() {
        idpProvider = new DefaultIdpProvider();
    }

    @Test
    void whenCreatingClientAccount_thenNoExceptionIsThrown() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // When & Then
        assertDoesNotThrow(() -> idpProvider.createClientAccount(email, password));
    }
}
