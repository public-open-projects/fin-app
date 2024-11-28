package com.socrates.fin_app.identity.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void whenCreatingAdmin_thenIdIsGenerated() {
        // Given
        String email = "admin@example.com";
        String password = "adminPass123";

        // When
        Admin admin = new Admin(email, password);

        // Then
        assertNotNull(admin.getId());
        assertEquals(36, admin.getId().length()); // UUID length
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
    }
}
