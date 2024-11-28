package com.socrates.fin_app.identity.domain.entities;

import com.socrates.fin_app.common.annotations.DomainEntity;
import jakarta.persistence.*;
import java.util.UUID;

@DomainEntity
@Entity
@Table(name = "profiles")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile {
    @Id
    private String id;
    
    private String password;
    private String email;
    private ProfileType type;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    protected Profile() {
        // JPA
    }
    
    protected Profile(String password, ProfileType type) {
        this.id = UUID.randomUUID().toString();
        this.password = password;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public ProfileType getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
