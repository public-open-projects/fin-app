package com.socrates.fin_app.identity.domain.entities;

import com.socrates.fin_app.common.annotations.DomainEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@DomainEntity
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String email;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Profile> profiles = new HashSet<>();
    
    protected User() {
        // JPA
    }
    
    public User(String email) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
        profile.setUser(this);
    }

    public void removeProfile(Profile profile) {
        profiles.remove(profile);
        profile.setUser(null);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }
}
