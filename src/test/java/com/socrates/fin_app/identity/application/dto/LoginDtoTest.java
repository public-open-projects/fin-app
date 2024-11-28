package com.socrates.fin_app.identity.application.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginDtoTest {

    @Test
    void whenCreatingLoginDto_thenFieldsAreCorrect() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // When
        LoginDto loginDto = new LoginDto(email, password);

        // Then
        assertEquals(email, loginDto.email());
        assertEquals(password, loginDto.password());
    }

    @Test
    void whenComparingEqualLoginDtos_thenTheyAreEqual() {
        // Given
        LoginDto dto1 = new LoginDto("test@example.com", "password123");
        LoginDto dto2 = new LoginDto("test@example.com", "password123");

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
