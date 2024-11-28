package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.security.PasswordEncoder;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateClientUseCaseImpl implements AuthenticateClientUseCase {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    
    public AuthenticateClientUseCaseImpl(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        // Find client by email
        var client = clientRepository.findByEmail(request.email())
            .orElseThrow(() -> new AuthenticationException("Invalid credentials"));
            
        // Verify password
        if (!passwordEncoder.matches(request.password(), client.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
        
        // Generate token
        String token = tokenProvider.createToken(client.getEmail(), "CLIENT");
        
        return new AuthenticationResponse(
            token,
            client.getEmail()
        );
    }
}
