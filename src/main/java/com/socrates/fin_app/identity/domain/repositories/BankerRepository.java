package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.BankerProfile;
import org.springframework.data.repository.CrudRepository;

public interface BankerRepository extends CrudRepository<BankerProfile, String> {
    boolean existsByEmail(String email);
}
