package com.socrates.fin_app.identity.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_profiles")
public class AdminProfile extends Profile {
    private String department;
    private String role;
    
    protected AdminProfile() {
        // JPA
    }
    
    public AdminProfile(String password) {
        super(password, ProfileType.ADMIN);
    }

    public void updateProfile(String department, String role) {
        this.department = department;
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }
}
