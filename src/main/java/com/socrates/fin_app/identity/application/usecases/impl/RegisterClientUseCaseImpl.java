package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import com.socrates.fin_app.identity.domain.entities.Client;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterClientUseCaseImpl implements RegisterClientUseCase {
    
    private final ClientRepository clientRepository;
    
    public RegisterClientUseCaseImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    @Override
    public RegistrationResponse execute(ClientRegistrationRequest request) {
        Client client = new Client(request.email(), request.password());
        Client savedClient = clientRepository.save(client);
        
        return new RegistrationResponse(
            savedClient.getId(),
            savedClient.getEmail()
        );
    }
}
