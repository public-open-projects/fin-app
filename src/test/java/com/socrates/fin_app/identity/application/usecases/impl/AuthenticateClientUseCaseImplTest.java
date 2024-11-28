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
        
        when(clientRepository.existsByEmail(email)).thenReturn(true);
        when(idpProvider.authenticateUser(email, password)).thenReturn(expectedToken);

        // When
        AuthenticationResponse response = authenticateClientUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.token());
        assertEquals(email, response.email());
        
        verify(idpProvider).authenticateUser(email, password);
        verify(clientRepository).existsByEmail(email);
    }

    @Test
    void whenClientDoesNotExist_thenThrowAuthenticationException() {
        // Given
        String email = "nonexistent@example.com";
        String password = "password123";
        LoginRequest request = new LoginRequest(email, password);
        
        when(clientRepository.existsByEmail(email)).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(AuthenticationException.class,
            () -> authenticateClientUseCase.execute(request));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(idpProvider, never()).authenticateUser(anyString(), anyString());
    }

    @Test
    void whenIdpProviderFails_thenThrowAuthenticationException() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        LoginRequest request = new LoginRequest(email, password);
        
        when(clientRepository.existsByEmail(email)).thenReturn(true);
        when(idpProvider.authenticateUser(email, password)).thenThrow(new RuntimeException("IDP Error"));

        // When & Then
        Exception exception = assertThrows(AuthenticationException.class,
            () -> authenticateClientUseCase.execute(request));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void whenIdpReturnsEmptyToken_thenThrowAuthenticationException() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        LoginRequest request = new LoginRequest(email, password);
        
        when(clientRepository.existsByEmail(email)).thenReturn(true);
        when(idpProvider.authenticateUser(email, password)).thenReturn("");

        // When & Then
        Exception exception = assertThrows(AuthenticationException.class,
            () -> authenticateClientUseCase.execute(request));
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
