package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void whenFindByEmail_thenReturnClient() {
        // Given
        ClientProfile client = new ClientProfile("test@example.com", "password123");
        entityManager.persist(client);
        entityManager.flush();

        // When
        var found = clientRepository.findByEmail(client.getEmail());

        // Then
        assertTrue(found.isPresent());
        assertEquals(client.getEmail(), found.get().getEmail());
    }

    @Test
    void whenExistsByEmail_thenReturnTrue() {
        // Given
        ClientProfile client = new ClientProfile("test@example.com", "password123");
        entityManager.persist(client);
        entityManager.flush();

        // When
        boolean exists = clientRepository.existsByEmail(client.getEmail());

        // Then
        assertTrue(exists);
    }

    @Test
    void whenExistsByEmail_withNonExistentEmail_thenReturnFalse() {
        // When
        boolean exists = clientRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }
}
