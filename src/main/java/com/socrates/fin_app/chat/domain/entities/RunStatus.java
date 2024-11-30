package com.socrates.fin_app.chat.domain.entities;

import java.util.List;

public record RunStatus(
    Status status,
    List<String> messages
) {
    public enum Status {
        QUEUED,
        IN_PROGRESS, 
        COMPLETED,
        FAILED,
        CANCELLED,
        EXPIRED
    }
}
