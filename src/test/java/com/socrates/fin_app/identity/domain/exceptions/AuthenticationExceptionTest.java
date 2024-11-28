package com.socrates.fin_app.identity.domain.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationExceptionTest {

    @Test
    void whenCreatingWithMessage_thenMessageIsStored() {
        // Given
        String message = "Invalid credentials";

        // When
        AuthenticationException exception = new AuthenticationException(message);

        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void whenCreatingWithMessageAndCause_thenBothAreStored() {
        // Given
        String message = "Invalid credentials";
        Throwable cause = new RuntimeException("Root cause");

        // When
        AuthenticationException exception = new AuthenticationException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
