package com.socrates.fin_app.identity.domain.repositories;

import com.socrates.fin_app.identity.domain.entities.AdminProfile;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<AdminProfile, String> {
    boolean existsByEmail(String email);
}
