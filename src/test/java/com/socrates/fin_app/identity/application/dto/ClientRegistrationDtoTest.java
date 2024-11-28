package com.socrates.fin_app.identity.application.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientRegistrationDtoTest {

    @Test
    void whenCreatingClientRegistrationDto_thenFieldsAreCorrect() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // When
        ClientRegistrationDto dto = new ClientRegistrationDto(email, password);

        // Then
        assertEquals(email, dto.email());
        assertEquals(password, dto.password());
    }

    @Test
    void whenComparingEqualClientRegistrationDtos_thenTheyAreEqual() {
        // Given
        ClientRegistrationDto dto1 = new ClientRegistrationDto("test@example.com", "password123");
        ClientRegistrationDto dto2 = new ClientRegistrationDto("test@example.com", "password123");

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void whenCreatingDifferentDtos_thenTheyAreNotEqual() {
        // Given
        ClientRegistrationDto dto1 = new ClientRegistrationDto("test1@example.com", "password123");
        ClientRegistrationDto dto2 = new ClientRegistrationDto("test2@example.com", "password123");

        // Then
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}
