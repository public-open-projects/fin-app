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
    void whenCreatingProfileWithDifferentTypes_thenTypeIsCorrect() {
        // Given & When
        Profile clientProfile = new ClientProfile("password");
        Profile adminProfile = new AdminProfile("password");
        Profile bankerProfile = new BankerProfile("password");

        // Then
        assertEquals(ProfileType.CLIENT, clientProfile.getType());
        assertEquals(ProfileType.ADMIN, adminProfile.getType());
        assertEquals(ProfileType.BANKER, bankerProfile.getType());
    }

    @Test
    void whenSettingNullEmail_thenEmailIsUpdated() {
        // Given
        Profile profile = new ClientProfile("password");
        
        // When
        profile.setEmail(null);
        
        // Then
        assertNull(profile.getEmail());
    }

    @Test
    void whenCreatingProfile_thenIdIsGenerated() {
        // Given & When
        ClientProfile profile = new ClientProfile("password");

        // Then
        assertNotNull(profile.getId());
        assertEquals(36, profile.getId().length()); // UUID length
    }

    @Test
    void whenAddingProfileToUser_thenProfileIsLinked() {
        // Given
        User user = new User("test@example.com");
        ClientProfile profile = new ClientProfile("password");

        // When
        user.addProfile(profile);

        // Then
        assertTrue(user.getProfiles().contains(profile));
        assertEquals(user, profile.getUser());
    }

    @Test
    void whenRemovingProfileFromUser_thenProfileIsUnlinked() {
        // Given
        User user = new User("test@example.com");
        ClientProfile profile = new ClientProfile("password");
        user.addProfile(profile);

        // When
        user.removeProfile(profile);

        // Then
        assertFalse(user.getProfiles().contains(profile));
        assertNull(profile.getUser());
    }

    @Test
    void whenCreatingUser_thenFieldsAreInitialized() {
        // Given
        String email = "test@example.com";

        // When
        User user = new User(email);

        // Then
        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertNotNull(user.getProfiles());
        assertTrue(user.getProfiles().isEmpty());
    }

    @Test
    void whenUsingDefaultConstructor_thenObjectIsCreated() {
        // When
        Profile profile = new ClientProfile();
        User user = new User();

        // Then
        assertNotNull(profile);
        assertNotNull(user);
    }

    @Test
    void whenSettingUserOnProfile_thenUserIsSet() {
        // Given
        User user = new User("test@example.com");
        ClientProfile profile = new ClientProfile("password");

        // When
        profile.setUser(user);

        // Then
        assertEquals(user, profile.getUser());
    }
}
