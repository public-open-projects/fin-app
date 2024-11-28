package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ResetPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.ResetPasswordResponse;
import com.socrates.fin_app.identity.domain.entities.Client;
import com.socrates.fin_app.identity.domain.exceptions.InvalidTokenException;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.security.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    private ResetPasswordUseCaseImpl resetPasswordUseCase;

    @BeforeEach
    void setUp() {
        resetPasswordUseCase = new ResetPasswordUseCaseImpl(clientRepository, passwordEncoder);
    }

    @Test
    void whenValidToken_thenResetPassword() {
        // Given
        String token = "valid-token";
        String newPassword = "newPassword123";
        String email = "test@example.com";
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword);
        Client client = new Client(email, "oldPassword");
        
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        ResetPasswordResponse response = resetPasswordUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals("Password reset successfully", response.message());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void whenInvalidToken_thenThrowException() {
        // Given
        String token = "invalid-token";
        ResetPasswordRequest request = new ResetPasswordRequest(token, "newPassword123");

        // When & Then
        assertThrows(InvalidTokenException.class, () -> resetPasswordUseCase.execute(request));
        verify(clientRepository, never()).save(any(Client.class));
    }
}
