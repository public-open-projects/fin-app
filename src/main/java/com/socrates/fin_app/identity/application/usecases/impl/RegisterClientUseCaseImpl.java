package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterClientUseCaseImpl implements RegisterClientUseCase {
    private static final Logger logger = LoggerFactory.getLogger(RegisterClientUseCaseImpl.class);
    
    private final ClientRepository clientRepository;
    private final IdpProvider idpProvider;
    
    public RegisterClientUseCaseImpl(
            ClientRepository clientRepository,
            IdpProvider idpProvider) {
        this.clientRepository = clientRepository;
        this.idpProvider = idpProvider;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    @Override
    @Transactional
    public RegistrationResponse execute(ClientRegistrationRequest request) {
        try {
            // Validate email format
            if (!isValidEmail(request.email())) {
                throw new IllegalArgumentException("Invalid email format");
            }
            
            // Check if email already exists
            if (clientRepository.existsByEmail(request.email())) {
                logger.warn("Registration attempt with existing email: {}", request.email());
                throw new IllegalStateException("Email already registered");
            }
            
            // Create account in IDP first
            try {
                idpProvider.createClientAccount(request.email(), request.password());
            } catch (Exception e) {
                logger.error("IDP account creation failed for email: {}", request.email(), e);
                throw new IllegalStateException("Registration failed: " + e.getMessage());
            }
            
            // If IDP creation successful, create local client record
            ClientProfile client = new ClientProfile(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName()
            );
            ClientProfile savedClient = clientRepository.save(client);
            
            logger.info("Successfully registered new client with email: {}", request.email());
            
            // Return success response
            return new RegistrationResponse(
                savedClient.getId(),
                savedClient.getEmail()
            );
            
        } catch (IllegalStateException e) {
            throw e; // Re-throw validation errors
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            throw new IllegalStateException("Registration failed: " + e.getMessage());
        }
    }
}
