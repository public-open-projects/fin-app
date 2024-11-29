package com.socrates.fin_app.chat.domain.exceptions;

public class InvalidChatCommandException extends RuntimeException {
    public InvalidChatCommandException(String message) {
        super(message);
    }
    
    public InvalidChatCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
