package com.socrates.fin_app.identity.interfaces;

import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import com.socrates.fin_app.identity.domain.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdentityExceptionHandlerTest {

    private IdentityExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new IdentityExceptionHandler();
    }

    @Test
    void whenHandlingAuthenticationException_thenReturnsUnauthorized() {
        // Given
        AuthenticationException ex = new AuthenticationException("Invalid credentials");

        // When
        ResponseEntity<IdentityExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleAuthenticationException(ex);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid credentials", response.getBody().message());
        assertEquals("UNAUTHORIZED", response.getBody().error());
    }

    @Test
    void whenHandlingUserNotFoundException_thenReturnsNotFound() {
        // Given
        UserNotFoundException ex = new UserNotFoundException("User not found");

        // When
        ResponseEntity<IdentityExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleUserNotFoundException(ex);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().message());
        assertEquals("NOT_FOUND", response.getBody().error());
    }

    @Test
    void whenHandlingIllegalArgumentException_thenReturnsBadRequest() {
        // Given
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");

        // When
        ResponseEntity<IdentityExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleIllegalArgumentException(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().message());
        assertEquals("BAD_REQUEST", response.getBody().error());
    }

    @Test
    void whenHandlingMethodArgumentNotValidException_thenReturnsBadRequest() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field", "error message"));
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // When
        ResponseEntity<IdentityExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleValidationExceptions(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("field: error message"));
        assertEquals("BAD_REQUEST", response.getBody().error());
    }

    @Test
    void whenHandlingGenericException_thenReturnsInternalServerError() {
        // Given
        Exception ex = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<IdentityExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleGenericException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().error());
    }
}
