package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.Banker;
import org.springframework.data.repository.CrudRepository;

public interface BankerRepository extends CrudRepository<Banker, String> {
    boolean existsByEmail(String email);
}
