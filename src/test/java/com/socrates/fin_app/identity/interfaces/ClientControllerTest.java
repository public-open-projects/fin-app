package com.socrates.fin_app.identity.interfaces;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.request.UpdateProfileRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.application.usecases.InitiatePasswordRecoveryUseCase;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import com.socrates.fin_app.identity.application.usecases.UpdateClientProfileUseCase;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import com.socrates.fin_app.identity.domain.exceptions.UserNotFoundException;

@WebMvcTest(ClientController.class)
@Import(TestSecurityConfig.class)
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
    @WithMockUser
    void whenValidRegistration_thenReturns200() throws Exception {
        // Given
        ClientRegistrationRequest request = new ClientRegistrationRequest(
            "test@example.com", "password123", "John", "Doe"
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
    void whenInvalidEmail_thenReturns400() throws Exception {
        // Given
        ClientRegistrationRequest request = new ClientRegistrationRequest(
            "invalid-email", "password123", "John", "Doe"
        );
        
        when(registerClientUseCase.execute(any(ClientRegistrationRequest.class)))
            .thenThrow(new IllegalArgumentException("Invalid email format"));

        // When & Then
        mockMvc.perform(post("/api/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("email: Invalid email format"));
    }

    @Test
    @WithMockUser
    void whenLoginWithInvalidCredentials_thenReturns401() throws Exception {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "wrongpass");
        
        when(authenticateClientUseCase.execute(any(LoginRequest.class)))
            .thenThrow(new AuthenticationException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/clients/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void whenForgotPasswordWithUnregisteredEmail_thenReturns404() throws Exception {
        // Given
        ForgotPasswordRequest request = new ForgotPasswordRequest("unknown@example.com");
        
        when(initiatePasswordRecoveryUseCase.execute(any(ForgotPasswordRequest.class)))
            .thenThrow(new UserNotFoundException("Email not found"));

        // When & Then
        mockMvc.perform(post("/api/clients/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenUpdateProfileWithInvalidId_thenReturns404() throws Exception {
        // Given
        String clientId = "non-existent-id";
        UpdateProfileRequest request = new UpdateProfileRequest(
            "test@example.com", "John", "Doe", "1234567890"
        );
        
        when(updateClientProfileUseCase.execute(eq(clientId), any(UpdateProfileRequest.class)))
            .thenThrow(new UserNotFoundException("Client not found"));

        // When & Then
        mockMvc.perform(put("/api/clients/" + clientId + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Client not found"));
    }

    @Test
    @WithMockUser
    void whenUpdateProfileWithInvalidData_thenReturns400() throws Exception {
        // Given
        String clientId = "test-id";
        UpdateProfileRequest request = new UpdateProfileRequest(
            "invalid-email", "", "", ""  // Invalid email and empty names
        );

        // When & Then
        mockMvc.perform(put("/api/clients/" + clientId + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void whenServerError_thenReturns500() throws Exception {
        // Given
        ClientRegistrationRequest request = new ClientRegistrationRequest(
            "test@example.com", "password123", "John", "Doe"
        );
        
        when(registerClientUseCase.execute(any(ClientRegistrationRequest.class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        mockMvc.perform(post("/api/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An unexpected error occurred"));
    }
}
