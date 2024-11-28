package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.UpdateProfileRequest;
import com.socrates.fin_app.identity.application.dto.response.ProfileResponse;
import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateClientProfileUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    private UpdateClientProfileUseCaseImpl updateClientProfileUseCase;

    @BeforeEach
    void setUp() {
        updateClientProfileUseCase = new UpdateClientProfileUseCaseImpl(clientRepository);
    }

    @Test
    void whenValidUpdate_thenProfileIsUpdated() {
        // Given
        String clientId = "test-id";
        String email = "new@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "1234567890";

        ClientProfile existingClient = new ClientProfile("old@example.com", "password");
        UpdateProfileRequest request = new UpdateProfileRequest(email, firstName, lastName, phoneNumber);
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.existsByEmail(email)).thenReturn(false);
        when(clientRepository.save(any(ClientProfile.class))).thenReturn(existingClient);

        // When
        ProfileResponse response = updateClientProfileUseCase.execute(clientId, request);

        // Then
        assertNotNull(response);
        assertEquals(email, response.email());
        assertEquals(firstName, response.firstName());
        assertEquals(lastName, response.lastName());
        assertEquals(phoneNumber, response.phoneNumber());
        verify(clientRepository).save(any(ClientProfile.class));
    }

    @Test
    void whenClientNotFound_thenThrowException() {
        // Given
        String clientId = "non-existent-id";
        UpdateProfileRequest request = new UpdateProfileRequest(
            "test@example.com", "John", "Doe", "1234567890"
        );
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class,
            () -> updateClientProfileUseCase.execute(clientId, request));
        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any(ClientProfile.class));
    }

    @Test
    void whenEmailAlreadyInUse_thenThrowException() {
        // Given
        String clientId = "test-id";
        String newEmail = "existing@example.com";
        ClientProfile existingClient = new ClientProfile("old@example.com", "password");
        UpdateProfileRequest request = new UpdateProfileRequest(
            newEmail, "John", "Doe", "1234567890"
        );
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.existsByEmail(newEmail)).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class,
            () -> updateClientProfileUseCase.execute(clientId, request));
        assertEquals("Email already in use", exception.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void whenNoClientId_thenThrowException() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest(
            "test@example.com", "John", "Doe", "1234567890"
        );

        // When & Then
        Exception exception = assertThrows(UnsupportedOperationException.class,
            () -> updateClientProfileUseCase.execute(request));
        assertEquals("Client ID is required", exception.getMessage());
    }
}
