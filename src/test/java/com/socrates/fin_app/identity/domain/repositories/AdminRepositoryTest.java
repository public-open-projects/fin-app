package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.Admin;
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
        Admin admin = new Admin("admin@example.com", "password123");

        // When
        Admin savedAdmin = adminRepository.save(admin);

        // Then
        assertNotNull(savedAdmin.getId());
        assertTrue(adminRepository.existsByEmail("admin@example.com"));
    }

    @Test
    void whenCheckingExistingEmail_thenReturnTrue() {
        // Given
        Admin admin = new Admin("admin@example.com", "password123");
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
