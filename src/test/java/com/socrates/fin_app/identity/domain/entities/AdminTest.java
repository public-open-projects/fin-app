package com.socrates.fin_app.identity.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminProfileTest {

    @Test
    void whenCreatingAdminProfile_thenIdIsGenerated() {
        // Given
        String email = "admin@example.com";
        String password = "adminPass123";

        // When
        AdminProfile admin = new AdminProfile(email, password);

        // Then
        assertNotNull(admin.getId());
        assertEquals(36, admin.getId().length()); // UUID length
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
    }
}
