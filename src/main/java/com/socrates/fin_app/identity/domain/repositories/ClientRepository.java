package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {
    // Will implement later
}
