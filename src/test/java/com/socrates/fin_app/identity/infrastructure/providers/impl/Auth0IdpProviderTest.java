package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.CreatedUser;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.Request;
import com.auth0.net.Response;
import com.auth0.net.SignUpRequest;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Auth0IdpProviderTest {

    @Mock
    private AuthAPI auth0Client;

    @Mock
    private SignUpRequest signUpRequest;
    
    @Mock
    private Response<CreatedUser> response;

    private Auth0IdpProvider auth0IdpProvider;

    @BeforeEach
    void setUp() {
        auth0IdpProvider = new Auth0IdpProvider(auth0Client, "test-connection");
    }

    @Test
    void whenAuthenticatingValidUser_thenSucceeds() throws Auth0Exception {
        // Given
        String email = "test@example.com";
        String password = "password123";
        TokenHolder tokenHolder = mock(TokenHolder.class);
        Request<TokenHolder> authRequest = mock(Request.class);
        Response<TokenHolder> tokenResponse = mock(Response.class);
        
        when(tokenHolder.getIdToken()).thenReturn("valid.jwt.token");
        when(auth0Client.login(email, password)).thenReturn(authRequest);
        when(authRequest.setRealm(anyString())).thenReturn(authRequest);
        when(authRequest.execute()).thenReturn(tokenResponse);
        when(tokenResponse.getBody()).thenReturn(tokenHolder);

        // When
        String token = auth0IdpProvider.authenticateUser(email, password);

        // Then
        assertNotNull(token);
        assertEquals("valid.jwt.token", token);
    }

    @Test
    void whenAuthenticatingWithNullToken_thenThrowsException() throws Auth0Exception {
        // Given
        TokenHolder tokenHolder = mock(TokenHolder.class);
        Request<TokenHolder> authRequest = mock(Request.class);
        Response<TokenHolder> tokenResponse = mock(Response.class);
        
        when(tokenHolder.getIdToken()).thenReturn(null);
        when(auth0Client.login(anyString(), anyString())).thenReturn(authRequest);
        when(authRequest.setRealm(anyString())).thenReturn(authRequest);
        when(authRequest.execute()).thenReturn(tokenResponse);
        when(tokenResponse.getBody()).thenReturn(tokenHolder);

        // When & Then
        assertThrows(AuthenticationException.class, () ->
            auth0IdpProvider.authenticateUser("test@example.com", "password123"));
    }

    @Test
    void whenCreatingAccountWithNullResponse_thenThrowsException() throws Auth0Exception {
        // Given
        Request<CreatedUser> request = mock(Request.class);
        Response<CreatedUser> nullResponse = mock(Response.class);
        
        when(auth0Client.signUp(anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(nullResponse);
        when(nullResponse.getBody()).thenReturn(null);

        // When & Then
        assertThrows(AuthenticationException.class, () ->
            auth0IdpProvider.createClientAccount("test@example.com", "password123"));
    }

    @Test
    void whenCreatingValidAccount_thenSucceeds() throws Auth0Exception {
        // Given
        CreatedUser createdUser = mock(CreatedUser.class);
        when(createdUser.getEmail()).thenReturn("test@example.com");
        
        when(auth0Client.signUp(anyString(), anyString(), anyString()))
            .thenReturn(signUpRequest);
        when(signUpRequest.execute()).thenReturn(response);
        when(response.getBody()).thenReturn(createdUser);

        // When & Then
        assertDoesNotThrow(() -> 
            auth0IdpProvider.createClientAccount("test@example.com", "password123")
        );
    }

    @Test
    void whenAuth0Fails_thenThrowsAuthenticationException() throws Auth0Exception {
        // Given
        when(auth0Client.signUp(anyString(), anyString(), anyString()))
            .thenReturn(signUpRequest);
        when(signUpRequest.execute()).thenThrow(new Auth0Exception("Auth0 error"));

        // When & Then
        assertThrows(AuthenticationException.class, () ->
            auth0IdpProvider.createClientAccount("test@example.com", "password123")
        );
    }
}
