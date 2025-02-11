package com.socrates.fin_app.identity.infrastructure.services.impl;

import com.socrates.fin_app.identity.infrastructure.services.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class DefaultNotificationService implements NotificationService {
    @Override
    public void sendPasswordRecoveryEmail(String email, String recoveryToken) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (recoveryToken == null || recoveryToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Recovery token cannot be null or empty");
        }
        
        // TODO: Implement actual email sending logic
        // For now, just log the action
        System.out.println("Sending password recovery email to: " + email);
        System.out.println("Recovery token: " + recoveryToken);
    }
}
