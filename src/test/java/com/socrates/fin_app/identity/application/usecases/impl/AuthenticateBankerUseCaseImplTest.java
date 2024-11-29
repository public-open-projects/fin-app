package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateBankerUseCaseImplTest {

    @Mock
    private IdpProvider idpProvider;

    private AuthenticateBankerUseCaseImpl authenticateBankerUseCase;

    @BeforeEach
    void setUp() {
        authenticateBankerUseCase = new AuthenticateBankerUseCaseImpl(idpProvider);
    }

    @Test
    void whenValidCredentials_thenAuthenticateSuccessfully() {
        // Given
        LoginRequest request = new LoginRequest("banker@example.com", "password123");
        String expectedToken = "jwt-token";
        
        when(idpProvider.authenticateUser(anyString(), anyString()))
            .thenReturn(expectedToken);

        // When
        AuthenticationResponse response = authenticateBankerUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.token());
        assertEquals(request.email(), response.email());
    }
}
