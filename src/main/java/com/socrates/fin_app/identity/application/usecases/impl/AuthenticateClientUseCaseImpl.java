package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateClientUseCaseImpl implements AuthenticateClientUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthenticateClientUseCaseImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        // TODO: Add actual authentication logic
        String token = jwtTokenProvider.createToken(request.email(), "CLIENT");
        
        return new AuthenticationResponse(
            token,
            request.email()
        );
    }
}
