package com.socrates.fin_app.identity.application.usecases.impl;

import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateBankerUseCase;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateBankerUseCaseImpl implements AuthenticateBankerUseCase {
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthenticateBankerUseCaseImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public AuthenticationResponse execute(LoginRequest request) {
        // TODO: Add actual banker authentication logic
        String token = jwtTokenProvider.createToken(request.email(), "BANKER");
        return new AuthenticationResponse(token, request.email());
    }
}
