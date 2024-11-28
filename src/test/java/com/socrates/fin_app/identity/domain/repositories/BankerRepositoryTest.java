package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.BankerProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BankerRepositoryTest {

    @Autowired
    private BankerRepository bankerRepository;

    @Test
    void whenSavingBanker_thenBankerIsPersisted() {
        // Given
        BankerProfile banker = new BankerProfile("banker@example.com", "password123");

        // When
        Banker savedBanker = bankerRepository.save(banker);

        // Then
        assertNotNull(savedBanker.getId());
        assertTrue(bankerRepository.existsByEmail("banker@example.com"));
    }

    @Test
    void whenCheckingExistingEmail_thenReturnTrue() {
        // Given
        Banker banker = new Banker("banker@example.com", "password123");
        bankerRepository.save(banker);

        // When
        boolean exists = bankerRepository.existsByEmail("banker@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void whenCheckingNonExistingEmail_thenReturnFalse() {
        // When
        boolean exists = bankerRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }
}
