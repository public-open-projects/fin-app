package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateAdminUseCase;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateAdminUseCaseImpl implements AuthenticateAdminUseCase {
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthenticateAdminUseCaseImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        // TODO: Add actual admin authentication logic
        String token = jwtTokenProvider.createToken(request.email(), "ADMIN");
        return new AuthenticationResponse(token, request.email());
    }
}
