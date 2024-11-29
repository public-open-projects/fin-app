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
    private String userId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    protected ChatSession() {
        // JPA
    }
    
    public ChatSession(String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.status = "ACTIVE";
        this.startTime = LocalDateTime.now();
    }
    
    public String getId() { 
        return id; 
    }
    
    public String getUserId() { 
        return userId; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public LocalDateTime getStartTime() { 
        return startTime; 
    }
    
    public LocalDateTime getEndTime() { 
        return endTime; 
    }
}
