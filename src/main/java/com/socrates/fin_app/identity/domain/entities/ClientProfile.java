package com.socrates.fin_app.identity.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_profiles")
public class ClientProfile extends Profile {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    protected ClientProfile() {
        // JPA
    }
    
    public ClientProfile(String password) {
        super(password, ProfileType.CLIENT);
    }

    public void updateProfile(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
package com.socrates.fin_app.identity.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_profiles")
public class ClientProfile extends Profile {
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    protected ClientProfile() {
        // JPA
    }
    
    public ClientProfile(String password) {
        super(password, ProfileType.CLIENT);
    }
    
    public ClientProfile(String email, String password) {
        this(password);
        setEmail(email);
    }

    public void updateProfile(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
