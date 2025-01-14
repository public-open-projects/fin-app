package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.AdminProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void whenSavingAdmin_thenAdminIsPersisted() {
        // Given
        AdminProfile admin = new AdminProfile("admin@example.com", "password123");

        // When
        AdminProfile savedAdmin = adminRepository.save(admin);

        // Then
        assertNotNull(savedAdmin.getId());
        assertTrue(adminRepository.existsByEmail("admin@example.com"));
    }

    @Test
    void whenCheckingExistingEmail_thenReturnTrue() {
        // Given
        AdminProfile admin = new AdminProfile("admin@example.com", "password123");
        adminRepository.save(admin);

        // When
        boolean exists = adminRepository.existsByEmail("admin@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void whenCheckingNonExistingEmail_thenReturnFalse() {
        // When
        boolean exists = adminRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }
}
