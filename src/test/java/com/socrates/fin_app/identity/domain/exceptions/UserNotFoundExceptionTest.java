package com.socrates.fin_app.identity.domain.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void whenCreatingWithMessage_thenMessageIsStored() {
        // Given
        String message = "User not found with id: 123";

        // When
        UserNotFoundException exception = new UserNotFoundException(message);

        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void whenCreatingWithMessageAndCause_thenBothAreStored() {
        // Given
        String message = "User not found with id: 123";
        Throwable cause = new RuntimeException("Root cause");

        // When
        UserNotFoundException exception = new UserNotFoundException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
