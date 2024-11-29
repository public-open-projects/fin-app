package com.socrates.fin_app.chat.domain.exceptions;

public class UnauthorizedChatFeatureException extends RuntimeException {
    public UnauthorizedChatFeatureException(String message) {
        super(message);
    }
}
