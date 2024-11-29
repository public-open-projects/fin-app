package com.socrates.fin_app.chat.domain.entities;

import com.socrates.fin_app.common.annotations.DomainEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@DomainEntity
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "session_id")
    private ChatSession session;
    
    private String content;
    private String senderType; // USER or SYSTEM
    private LocalDateTime timestamp;
    
    protected ChatMessage() {
        // JPA
    }
    
    public ChatMessage(ChatSession session, String content, String senderType) {
        this.id = UUID.randomUUID().toString();
        this.session = session;
        this.content = content;
        this.senderType = senderType;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getId() {
        return id;
    }
    
    public ChatSession getSession() {
        return session;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getSenderType() {
        return senderType;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
