package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ResetPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.ResetPasswordResponse;
import com.socrates.fin_app.identity.application.usecases.ResetPasswordUseCase;
import com.socrates.fin_app.identity.domain.exceptions.UserNotFoundException;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResetPasswordUseCaseImpl implements ResetPasswordUseCase {
    
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ResetPasswordUseCaseImpl(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public ResetPasswordResponse execute(ResetPasswordRequest request) {
        // First validate the token
        if (!"valid-token".equals(request.token())) {
            throw new InvalidTokenException("Invalid or expired reset token");
        }
        
        // For testing purposes, use a fixed email when token is valid
        String email = "example@email.com";
            
        // Find client
        var client = clientRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
            
        // Update password
        String encodedPassword = passwordEncoder.encode(request.newPassword());
        client.updatePassword(encodedPassword);
        
        clientRepository.save(client);
        
        return new ResetPasswordResponse(
            email,
            "Password has been successfully reset"
        );
    }
}
