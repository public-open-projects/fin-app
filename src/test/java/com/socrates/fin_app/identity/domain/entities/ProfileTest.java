package com.socrates.fin_app.identity.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void whenUpdatingPassword_thenPasswordIsUpdated() {
        // Given
        ClientProfile profile = new ClientProfile("oldPassword");
        String newPassword = "newPassword123";

        // When
        profile.updatePassword(newPassword);

        // Then
        assertEquals(newPassword, profile.getPassword());
    }

    @Test
    void whenSettingEmail_thenEmailIsSet() {
        // Given
        ClientProfile profile = new ClientProfile("password");
        String email = "test@example.com";

        // When
        profile.setEmail(email);

        // Then
        assertEquals(email, profile.getEmail());
    }

    @Test
    void whenCreatingProfile_thenTypeIsSet() {
        // Given & When
        ClientProfile clientProfile = new ClientProfile("password");
        AdminProfile adminProfile = new AdminProfile("password");
        BankerProfile bankerProfile = new BankerProfile("password");

        // Then
        assertEquals(ProfileType.CLIENT, clientProfile.getType());
        assertEquals(ProfileType.ADMIN, adminProfile.getType());
        assertEquals(ProfileType.BANKER, bankerProfile.getType());
    }

    @Test
    void whenCreatingProfile_thenIdIsGenerated() {
        // Given & When
        ClientProfile profile = new ClientProfile("password");

        // Then
        assertNotNull(profile.getId());
        assertEquals(36, profile.getId().length()); // UUID length
    }
}
