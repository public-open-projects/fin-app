package com.socrates.fin_app.identity.infrastructure.providers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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

    @Test
    void whenAuthenticatingUser_thenTokenIsReturned() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // When
        String token = idpProvider.authenticateUser(email, password);

        // Then
        assertNotNull(token);
        assertEquals("test-auth0-token", token);
    }
}
