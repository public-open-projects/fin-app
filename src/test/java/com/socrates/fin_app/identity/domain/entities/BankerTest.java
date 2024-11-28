package com.socrates.fin_app.identity.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankerProfileTest {

    @Test
    void whenCreatingBankerProfile_thenIdIsGenerated() {
        // Given
        String email = "banker@example.com";
        String password = "bankerPass123";

        // When
        BankerProfile banker = new BankerProfile(email, password);

        // Then
        assertNotNull(banker.getId());
        assertEquals(36, banker.getId().length()); // UUID length
        assertEquals(email, banker.getEmail());
        assertEquals(password, banker.getPassword());
    }

    @Test
    void whenUpdatingBankerProfile_thenFieldsAreUpdated() {
        // Given
        BankerProfile banker = new BankerProfile("banker@example.com", "password");
        String newBranch = "Main Branch";
        String newPosition = "Senior Banker";

        // When
        banker.updateProfile(newBranch, newPosition);

        // Then
        assertEquals(newBranch, banker.getBranch());
        assertEquals(newPosition, banker.getPosition());
    }
}
