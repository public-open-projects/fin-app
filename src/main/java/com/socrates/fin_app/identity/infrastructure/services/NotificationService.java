package com.socrates.fin_app.identity.infrastructure.services;

public interface NotificationService {
    void sendPasswordRecoveryEmail(String email, String recoveryToken);
}
