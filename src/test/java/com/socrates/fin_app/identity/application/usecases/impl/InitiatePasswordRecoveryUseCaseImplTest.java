package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitiatePasswordRecoveryUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private IdpProvider idpProvider;

    @Mock
    private NotificationService notificationService;

    private InitiatePasswordRecoveryUseCaseImpl initiatePasswordRecoveryUseCase;

    @BeforeEach
    void setUp() {
        initiatePasswordRecoveryUseCase = new InitiatePasswordRecoveryUseCaseImpl(
            clientRepository, idpProvider, notificationService);
    }

    @Test
    void whenValidEmail_thenRecoveryEmailIsSent() {
        // Given
        String email = "test@example.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        
        when(clientRepository.existsByEmail(email)).thenReturn(true);

        // When
        PasswordRecoveryResponse response = initiatePasswordRecoveryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(email, response.email());
        assertTrue(response.message().contains("sent successfully"));
        verify(notificationService).sendPasswordRecoveryEmail(eq(email), anyString());
    }

    @Test
    void whenEmailNotFound_thenThrowException() {
        // Given
        String email = "nonexistent@example.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        
        when(clientRepository.existsByEmail(email)).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, 
            () -> initiatePasswordRecoveryUseCase.execute(request));
        assertEquals("Email not found", exception.getMessage());
        verify(notificationService, never()).sendPasswordRecoveryEmail(anyString(), anyString());
    }

    @Test
    void whenNotificationFails_thenThrowException() {
        // Given
        String email = "test@example.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        
        when(clientRepository.existsByEmail(email)).thenReturn(true);
        doThrow(new RuntimeException("Email service down"))
            .when(notificationService).sendPasswordRecoveryEmail(anyString(), anyString());

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, 
            () -> initiatePasswordRecoveryUseCase.execute(request));
        assertTrue(exception.getMessage().contains("Failed to send recovery email"));
    }
}
