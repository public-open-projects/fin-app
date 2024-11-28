package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;
import com.socrates.fin_app.identity.application.usecases.InitiatePasswordRecoveryUseCase;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import com.socrates.fin_app.identity.infrastructure.services.NotificationService;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class InitiatePasswordRecoveryUseCaseImpl implements InitiatePasswordRecoveryUseCase {
    
    private final ClientRepository clientRepository;
    private final IdpProvider idpProvider;
    private final NotificationService notificationService;
    
    public InitiatePasswordRecoveryUseCaseImpl(
            ClientRepository clientRepository,
            IdpProvider idpProvider,
            NotificationService notificationService) {
        this.clientRepository = clientRepository;
        this.idpProvider = idpProvider;
        this.notificationService = notificationService;
    }
    
    @Override
    public PasswordRecoveryResponse execute(ForgotPasswordRequest request) {
        // Check if email exists
        if (!clientRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email not found");
        }
        
        // Generate recovery token
        String recoveryToken = UUID.randomUUID().toString();
        
        try {
            // Send recovery email
            notificationService.sendPasswordRecoveryEmail(request.email(), recoveryToken);
            
            return new PasswordRecoveryResponse(
                request.email(),
                "Password recovery email sent successfully"
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to send recovery email: " + e.getMessage());
        }
    }
}
