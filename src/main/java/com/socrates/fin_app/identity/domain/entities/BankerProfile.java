package com.socrates.fin_app.identity.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "banker_profiles")
public class BankerProfile extends Profile {
    private String branch;
    private String position;
    
    protected BankerProfile() {
        // JPA
    }
    
    public BankerProfile(String password) {
        super(password, ProfileType.BANKER);
    }

    public void updateProfile(String branch, String position) {
        this.branch = branch;
        this.position = position;
    }

    public String getBranch() {
        return branch;
    }

    public String getPosition() {
        return position;
    }
}
