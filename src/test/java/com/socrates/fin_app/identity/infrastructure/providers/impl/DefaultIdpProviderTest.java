package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DefaultIdpProviderTest {

    private DefaultIdpProvider idpProvider;
    
    @Mock
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tokenProvider.createToken(anyString(), anyString())).thenReturn("test-token");
        idpProvider = new DefaultIdpProvider(tokenProvider);
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
        assertEquals("test-token", token);
    }
}
