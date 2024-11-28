package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<ClientProfile, String> {
    boolean existsByEmail(String email);
    Optional<ClientProfile> findByEmail(String email);
}
