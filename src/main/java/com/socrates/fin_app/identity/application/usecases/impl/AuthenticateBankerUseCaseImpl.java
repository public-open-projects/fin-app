package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateBankerUseCase;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateBankerUseCaseImpl implements AuthenticateBankerUseCase {
    private final IdpProvider idpProvider;
    
    public AuthenticateBankerUseCaseImpl(IdpProvider idpProvider) {
        this.idpProvider = idpProvider;
    }
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        String token = idpProvider.authenticateUser(request.email(), request.password());
        return new AuthenticationResponse(token, request.email());
    }
}
