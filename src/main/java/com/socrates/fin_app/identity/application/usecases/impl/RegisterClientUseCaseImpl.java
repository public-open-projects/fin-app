package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import com.socrates.fin_app.identity.domain.entities.Client;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterClientUseCaseImpl implements RegisterClientUseCase {
    
    private final ClientRepository clientRepository;
    private final IdpProvider idpProvider;
    
    public RegisterClientUseCaseImpl(
            ClientRepository clientRepository,
            IdpProvider idpProvider) {
        this.clientRepository = clientRepository;
        this.idpProvider = idpProvider;
    }
    
    @Override
    @Transactional
    public RegistrationResponse execute(ClientRegistrationRequest request) {
        try {
            // Create account in IDP first
            idpProvider.createClientAccount(request.email(), request.password());
            
            // If IDP creation successful, create local client record
            Client client = new Client(request.email(), request.password());
            Client savedClient = clientRepository.save(client);
            
            // Return success response
            return new RegistrationResponse(
                savedClient.getId(),
                savedClient.getEmail()
            );
            
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate email case
            throw new IllegalStateException("Email already registered");
        } catch (Exception e) {
            // Handle other validation/processing errors
            throw new IllegalStateException("Registration failed: " + e.getMessage());
        }
    }
}
