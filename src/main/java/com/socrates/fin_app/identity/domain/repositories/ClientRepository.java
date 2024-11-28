package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.Client;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, String> {
    boolean existsByEmail(String email);
    Optional<Client> findByEmail(String email);
}
