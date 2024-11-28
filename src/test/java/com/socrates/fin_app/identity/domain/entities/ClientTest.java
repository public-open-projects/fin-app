package com.socrates.fin_app.identity.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void whenCreatingClient_thenIdIsGenerated() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // When
        Client client = new Client(email, password);

        // Then
        assertNotNull(client.getId());
        assertEquals(36, client.getId().length()); // UUID length
        assertEquals(email, client.getEmail());
        assertEquals(password, client.getPassword());
    }

    @Test
    void whenUpdatingProfile_thenAllFieldsAreUpdated() {
        // Given
        Client client = new Client("original@example.com", "password");
        String newEmail = "new@example.com";
        String newFirstName = "John";
        String newLastName = "Doe";
        String newPhone = "1234567890";

        // When
        client.updateProfile(newEmail, newFirstName, newLastName, newPhone);

        // Then
        assertEquals(newEmail, client.getEmail());
        assertEquals(newFirstName, client.getFirstName());
        assertEquals(newLastName, client.getLastName());
        assertEquals(newPhone, client.getPhoneNumber());
    }

    @Test
    void whenUpdatingPassword_thenPasswordIsUpdated() {
        // Given
        Client client = new Client("test@example.com", "oldPassword");
        String newPassword = "newPassword123";

        // When
        client.updatePassword(newPassword);

        // Then
        assertEquals(newPassword, client.getPassword());
    }
}
