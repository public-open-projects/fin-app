package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticateClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;
    
    @Mock
    private IdpProvider idpProvider;

    private AuthenticateClientUseCaseImpl authenticateClientUseCase;

    @BeforeEach
    void setUp() {
        authenticateClientUseCase = new AuthenticateClientUseCaseImpl(clientRepository, idpProvider);
    }

    @Test
    void whenValidCredentials_thenAuthenticationSucceeds() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        String expectedToken = "jwt-token";
        LoginRequest request = new LoginRequest(email, password);
        
        // Only stub the authenticateUser call since we removed the existsByEmail check
        when(idpProvider.authenticateUser(email, password)).thenReturn(expectedToken);

        // When
        AuthenticationResponse response = authenticateClientUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.token());
        assertEquals(email, response.email());
        
        // Verify the mock was called
        verify(idpProvider).authenticateUser(email, password);
    }
}
