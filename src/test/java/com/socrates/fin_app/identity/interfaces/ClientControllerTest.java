package com.socrates.fin_app.identity.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.application.usecases.InitiatePasswordRecoveryUseCase;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import com.socrates.fin_app.identity.application.usecases.UpdateClientProfileUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterClientUseCase registerClientUseCase;

    @MockBean
    private AuthenticateClientUseCase authenticateClientUseCase;

    @MockBean
    private InitiatePasswordRecoveryUseCase initiatePasswordRecoveryUseCase;

    @MockBean
    private UpdateClientProfileUseCase updateClientProfileUseCase;

    @Test
    void whenValidRegistration_thenReturns200() throws Exception {
        // Given
        ClientRegistrationRequest request = new ClientRegistrationRequest(
            "test@example.com", "password123"
        );
        RegistrationResponse response = new RegistrationResponse(
            "test-id", "test@example.com"
        );
        
        when(registerClientUseCase.execute(any(ClientRegistrationRequest.class)))
            .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("test-id"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void whenValidLogin_thenReturns200() throws Exception {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        AuthenticationResponse response = new AuthenticationResponse(
            "jwt-token", "test@example.com"
        );
        
        when(authenticateClientUseCase.execute(any(LoginRequest.class)))
            .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/clients/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void whenValidForgotPassword_thenReturns200() throws Exception {
        // Given
        ForgotPasswordRequest request = new ForgotPasswordRequest("test@example.com");
        PasswordRecoveryResponse response = new PasswordRecoveryResponse(
            "test@example.com", "Password recovery email sent successfully"
        );
        
        when(initiatePasswordRecoveryUseCase.execute(any(ForgotPasswordRequest.class)))
            .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/clients/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.message").value("Password recovery email sent successfully"));
    }

    @Test
    void whenInvalidEmail_thenReturns400() throws Exception {
        // Given
        ClientRegistrationRequest request = new ClientRegistrationRequest(
            "invalid-email", "password123"
        );

        // When & Then
        mockMvc.perform(post("/api/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
