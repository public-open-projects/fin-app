package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestJwtConfig.class)
@ActiveProfiles("test")
class DefaultIdpProviderTest {

    private DefaultIdpProvider idpProvider;
    
    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
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
        assertTrue(token.length() > 0);
    }
}
