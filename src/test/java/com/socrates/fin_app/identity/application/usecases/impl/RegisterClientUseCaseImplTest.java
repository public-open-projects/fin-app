package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import com.socrates.fin_app.identity.application.dto.request.UpdateProfileRequest;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class RegisterClientUseCaseImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private IdpProvider idpProvider;

    private RegisterClientUseCaseImpl registerClientUseCase;

    @BeforeEach
    void setUp() {
        registerClientUseCase = new RegisterClientUseCaseImpl(clientRepository, idpProvider);
    }

    @Test
    void whenValidRequest_thenClientIsRegistered() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        ClientRegistrationRequest request = new ClientRegistrationRequest(email, password, "John", "Doe");
        
        ClientProfile savedClient = new ClientProfile(email, password, "John", "Doe");
        when(clientRepository.save(any(ClientProfile.class))).thenReturn(savedClient);

        // When
        RegistrationResponse response = registerClientUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(email, response.email());
        assertNotNull(response.id());
        verify(idpProvider).createClientAccount(email, password);
        verify(clientRepository).save(any(ClientProfile.class));
    }

    @Test
    void whenEmailAlreadyExists_thenThrowException() {
        // Given
        String email = "existing@example.com";
        String password = "password123";
        ClientRegistrationRequest request = new ClientRegistrationRequest(email, password, "John", "Doe");
        
        when(clientRepository.existsByEmail(email)).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, 
            () -> registerClientUseCase.execute(request));
        assertEquals("Email already registered", exception.getMessage());
        verify(idpProvider, never()).createClientAccount(anyString(), anyString());
    }

    @Test
    void whenInvalidEmailFormat_thenThrowException() {
        // Given
        String email = "invalid-email";
        String password = "password123";
        ClientRegistrationRequest request = new ClientRegistrationRequest(email, password, "John", "Doe");

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> registerClientUseCase.execute(request));
        assertEquals("Invalid email format", exception.getMessage());
        verify(clientRepository, never()).existsByEmail(anyString());
        verify(idpProvider, never()).createClientAccount(anyString(), anyString());
    }

    @Test
    void whenUnexpectedErrorOccurs_thenThrowException() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        ClientRegistrationRequest request = new ClientRegistrationRequest(email, password, "John", "Doe");
        
        when(clientRepository.existsByEmail(email)).thenReturn(false);
        doNothing().when(idpProvider).createClientAccount(email, password);
        when(clientRepository.save(any(ClientProfile.class))).thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class,
            () -> registerClientUseCase.execute(request));
        assertTrue(exception.getMessage().contains("Registration failed"));
    }

    @Test
    void whenIdpProviderFails_thenThrowException() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        ClientRegistrationRequest request = new ClientRegistrationRequest(email, password, "John", "Doe");
        
        doThrow(new RuntimeException("IDP Error"))
            .when(idpProvider).createClientAccount(email, password);

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, 
            () -> registerClientUseCase.execute(request));
        assertTrue(exception.getMessage().contains("Registration failed"));
        verify(clientRepository, never()).save(any(ClientProfile.class));
    }
}
