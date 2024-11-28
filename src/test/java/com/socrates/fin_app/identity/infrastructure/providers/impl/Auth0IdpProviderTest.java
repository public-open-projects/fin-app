package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.CreatedUser;
import com.auth0.net.Request;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Auth0IdpProviderTest {

    @Mock
    private AuthAPI auth0Client;

    @Mock
    private Request<CreatedUser> request;

    private Auth0IdpProvider auth0IdpProvider;

    @BeforeEach
    void setUp() {
        auth0IdpProvider = new Auth0IdpProvider(auth0Client, "test-connection");
    }

    @Test
    void whenCreatingValidAccount_thenSucceeds() throws Auth0Exception {
        // Given
        CreatedUser createdUser = new CreatedUser();
        createdUser.setEmail("test@example.com");
        
        when(auth0Client.signUp(anyString(), anyString(), anyString()))
            .thenReturn(request);
        when(request.execute()).thenReturn(createdUser);

        // When & Then
        assertDoesNotThrow(() -> 
            auth0IdpProvider.createClientAccount("test@example.com", "password123")
        );
    }

    @Test
    void whenAuth0Fails_thenThrowsAuthenticationException() throws Auth0Exception {
        // Given
        when(auth0Client.signUp(anyString(), anyString(), anyString()))
            .thenReturn(request);
        when(request.execute()).thenThrow(new Auth0Exception("Auth0 error"));

        // When & Then
        assertThrows(AuthenticationException.class, () ->
            auth0IdpProvider.createClientAccount("test@example.com", "password123")
        );
    }
}
