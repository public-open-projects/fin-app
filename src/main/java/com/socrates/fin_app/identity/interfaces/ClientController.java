package com.socrates.fin_app.identity.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.socrates.fin_app.common.annotations.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import com.socrates.fin_app.identity.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@Tag(name = "Client Identity", description = "Client identity management APIs")
@ApiController
@RequestMapping("/api/clients")
@RestControllerAdvice
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
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

    @Operation(summary = "Register new client", 
              description = "Register a new client with email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody ClientRegistrationRequest request) {
        return ResponseEntity.ok(registerClientUseCase.execute(request));
    }

    @Operation(summary = "Client login", 
              description = "Authenticate client with email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticateClientUseCase.execute(request));
    }

    @Operation(summary = "Initiate password recovery", 
              description = "Send password recovery email to client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recovery email sent"),
        @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordRecoveryResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(initiatePasswordRecoveryUseCase.execute(request));
    }

    @PutMapping("/{clientId}/profile")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update client profile", 
              description = "Update client profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable String clientId,
            @Valid @RequestBody UpdateProfileRequest request) {
        try {
            return ResponseEntity.ok(updateClientProfileUseCase.execute(clientId, request));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred");
    }
}
