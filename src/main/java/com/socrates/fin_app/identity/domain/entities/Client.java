package com.socrates.fin_app.identity.domain.entities;

import com.socrates.fin_app.common.annotations.DomainEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@DomainEntity
@Entity
@Table(name = "clients")
public class Client {
    @Id
    private String id;
    private String email;
    private String password;
    
    protected Client() {
        // JPA
    }
    
    public Client(String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
