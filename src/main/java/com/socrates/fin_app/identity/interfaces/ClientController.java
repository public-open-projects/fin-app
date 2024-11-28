package com.socrates.fin_app.identity.interfaces;

import com.socrates.fin_app.common.annotations.ApiController;
import com.socrates.fin_app.identity.application.dto.request.ClientRegistrationRequest;
import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.request.UpdateProfileRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;
import com.socrates.fin_app.identity.application.dto.response.ProfileResponse;
import com.socrates.fin_app.identity.application.dto.response.RegistrationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateClientUseCase;
import com.socrates.fin_app.identity.application.usecases.InitiatePasswordRecoveryUseCase;
import com.socrates.fin_app.identity.application.usecases.RegisterClientUseCase;
import com.socrates.fin_app.identity.application.usecases.UpdateClientProfileUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/api/clients")
public class ClientController {
    private final RegisterClientUseCase registerClientUseCase;
    private final AuthenticateClientUseCase authenticateClientUseCase;
    private final InitiatePasswordRecoveryUseCase initiatePasswordRecoveryUseCase;
    private final UpdateClientProfileUseCase updateClientProfileUseCase;

    public ClientController(
            RegisterClientUseCase registerClientUseCase,
            AuthenticateClientUseCase authenticateClientUseCase,
            InitiatePasswordRecoveryUseCase initiatePasswordRecoveryUseCase,
            UpdateClientProfileUseCase updateClientProfileUseCase) {
        this.registerClientUseCase = registerClientUseCase;
        this.authenticateClientUseCase = authenticateClientUseCase;
        this.initiatePasswordRecoveryUseCase = initiatePasswordRecoveryUseCase;
        this.updateClientProfileUseCase = updateClientProfileUseCase;
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
