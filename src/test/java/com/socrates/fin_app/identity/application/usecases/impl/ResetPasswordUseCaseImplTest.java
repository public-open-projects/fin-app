package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ResetPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.ResetPasswordResponse;
import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import com.socrates.fin_app.identity.domain.exceptions.InvalidTokenException;
import com.socrates.fin_app.identity.domain.exceptions.UserNotFoundException;
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
        String token = "valid-token";  // Special test token
        String newPassword = "newPassword123";
        String email = "example@email.com";  // This matches what validateAndGetEmailFromToken returns
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword);
        ClientProfile client = new ClientProfile(email, "oldPassword");
        
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(clientRepository.save(any(ClientProfile.class))).thenReturn(client);

        // When
        ResetPasswordResponse response = resetPasswordUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(email, response.email());
        assertEquals("Password has been successfully reset", response.message());
        verify(passwordEncoder).encode(newPassword);
        verify(clientRepository).save(client);
    }

    @Test
    void whenInvalidToken_thenThrowException() {
        // Given
        String token = "invalid-token";
        ResetPasswordRequest request = new ResetPasswordRequest(token, "newPassword123");

        // When & Then
        InvalidTokenException exception = assertThrows(InvalidTokenException.class, 
            () -> resetPasswordUseCase.execute(request));
        assertEquals("Invalid or expired reset token", exception.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(clientRepository, never()).save(any(ClientProfile.class));
    }

    @Test
    void whenUserNotFound_thenThrowException() {
        // Given
        String token = "valid-token";  // Use valid token to get past validation
        String email = "example@email.com";
        ResetPasswordRequest request = new ResetPasswordRequest(token, "newPassword123");
        
        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, 
            () -> resetPasswordUseCase.execute(request));
        assertEquals("User not found", exception.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(clientRepository, never()).save(any(ClientProfile.class));
    }

    @Test
    void whenPasswordEncoderFails_thenThrowException() {
        // Given
        String token = "valid-token";  // Use valid token to get past validation
        String email = "example@email.com";
        String newPassword = "newPassword123";
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword);
        ClientProfile client = new ClientProfile(email, "oldPassword");
        
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(anyString())).thenThrow(new RuntimeException("Encoding failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> resetPasswordUseCase.execute(request));
        assertEquals("Encoding failed", exception.getMessage());
        verify(clientRepository, never()).save(any(ClientProfile.class));
    }

    @Test
    void whenSaveFails_thenThrowException() {
        // Given
        String token = "valid-token";  // Use valid token to get past validation
        String email = "example@email.com";
        String newPassword = "newPassword123";
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword);
        Client client = new Client(email, "oldPassword");
        
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(clientRepository.save(any(ClientProfile.class))).thenThrow(new RuntimeException("Save failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> resetPasswordUseCase.execute(request));
        assertEquals("Save failed", exception.getMessage());
        verify(passwordEncoder).encode(newPassword);
    }

    @Test
    void whenNullRequest_thenThrowException() {
        // When & Then
        assertThrows(NullPointerException.class, 
            () -> resetPasswordUseCase.execute(null));
        verify(clientRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(clientRepository, never()).save(any(Client.class));
    }
}
