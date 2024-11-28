package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.UpdateProfileRequest;
import com.socrates.fin_app.identity.application.dto.response.ProfileResponse;
import com.socrates.fin_app.identity.application.usecases.UpdateClientProfileUseCase;
import com.socrates.fin_app.identity.domain.entities.ClientProfile;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateClientProfileUseCaseImpl implements UpdateClientProfileUseCase {
    
    private final ClientRepository clientRepository;
    
    public UpdateClientProfileUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    @Override
    @Transactional
    public ProfileResponse execute(String clientId, UpdateProfileRequest request) {
        ClientProfile client = clientRepository.findById(clientId)
            .orElseThrow(() -> new IllegalStateException("Client not found"));
            
        // Check if email is already in use by another client
        if (!client.getEmail().equals(request.email()) && 
            clientRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already in use");
        }
        
        // Update profile
        client.updateProfile(
            request.firstName(),
            request.lastName(),
            request.phoneNumber()
        );
        
        ClientProfile updatedClient = clientRepository.save(client);
        
        return new ProfileResponse(
            updatedClient.getId(),
            updatedClient.getEmail(),
            updatedClient.getFirstName(),
            updatedClient.getLastName(),
            updatedClient.getPhoneNumber()
        );
    }

    @Override
    public ProfileResponse execute(UpdateProfileRequest request) {
        throw new UnsupportedOperationException("Client ID is required");
    }
}
