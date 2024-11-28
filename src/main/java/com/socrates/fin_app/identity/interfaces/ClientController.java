package com.socrates.fin_app.identity.interfaces;

import com.socrates.fin_app.common.annotations.ApiController;
import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.application.usecases.InitiatePasswordRecoveryUseCase;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiController
@RequestMapping("/api/clients")
public class ClientController {
    private final RegisterClientUseCase registerClientUseCase;
    private final AuthenticateClientUseCase authenticateClientUseCase;
    private final InitiatePasswordRecoveryUseCase initiatePasswordRecoveryUseCase;

    public ClientController(
            RegisterClientUseCase registerClientUseCase,
            AuthenticateClientUseCase authenticateClientUseCase,
            InitiatePasswordRecoveryUseCase initiatePasswordRecoveryUseCase) {
        this.registerClientUseCase = registerClientUseCase;
        this.authenticateClientUseCase = authenticateClientUseCase;
        this.initiatePasswordRecoveryUseCase = initiatePasswordRecoveryUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody ClientRegistrationRequest request) {
        return ResponseEntity.ok(registerClientUseCase.execute(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticateClientUseCase.execute(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordRecoveryResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(initiatePasswordRecoveryUseCase.execute(request));
    }
}
