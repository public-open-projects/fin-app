package com.socrates.fin_app.chat.domain.entities;

import com.socrates.fin_app.common.annotations.DomainEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@DomainEntity
@Entity
@Table(name = "chat_sessions")
public class ChatSession {
    @Id
    private String id;
    private String userId;  // null for unauthenticated sessions
    private String guestId; // used for unauthenticated sessions
    private String status;
    private boolean authenticated;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    protected ChatSession() {
        // JPA
    }
    
    // Constructor for authenticated sessions
    public ChatSession(String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.authenticated = true;
        this.status = "ACTIVE";
        this.startTime = LocalDateTime.now();
    }
    
    // Constructor for unauthenticated sessions
    public ChatSession(String guestId, boolean authenticated) {
        this.id = UUID.randomUUID().toString();
        this.guestId = guestId;
        this.authenticated = false;
        this.status = "ACTIVE";
        this.startTime = LocalDateTime.now();
    }
    
    public String getId() { 
        return id; 
    }
    
    public String getUserId() { 
        return userId; 
    }
    
    public String getGuestId() {
        return guestId;
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public LocalDateTime getStartTime() { 
        return startTime; 
    }
    
    public LocalDateTime getEndTime() { 
        return endTime; 
    }
}
