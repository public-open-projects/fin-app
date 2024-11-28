package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateClientUseCaseImpl implements AuthenticateClientUseCase {

    private final ClientRepository clientRepository;
    private final IdpProvider idpProvider;
    
    public AuthenticateClientUseCaseImpl(
            ClientRepository clientRepository,
            IdpProvider idpProvider) {
        this.clientRepository = clientRepository;
        this.idpProvider = idpProvider;
    }
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        try {
            // Authenticate with Auth0 and get JWT token
            String token = idpProvider.authenticateUser(request.email(), request.password());
            
            return new AuthenticationResponse(
                token,
                request.email()
            );
        } catch (Exception e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}
