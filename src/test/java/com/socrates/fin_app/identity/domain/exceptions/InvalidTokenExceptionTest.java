package com.socrates.fin_app.identity.domain.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidTokenExceptionTest {

    @Test
    void whenCreatingWithMessage_thenMessageIsStored() {
        // Given
        String message = "Token is expired";

        // When
        InvalidTokenException exception = new InvalidTokenException(message);

        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void whenCreatingWithMessageAndCause_thenBothAreStored() {
        // Given
        String message = "Token is expired";
        Throwable cause = new RuntimeException("Root cause");

        // When
        InvalidTokenException exception = new InvalidTokenException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
