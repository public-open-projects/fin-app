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
}
