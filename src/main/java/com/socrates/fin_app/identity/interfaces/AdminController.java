package com.socrates.fin_app.identity.interfaces;

import com.socrates.fin_app.common.annotations.ApiController;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateAdminUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/api/admins")
public class AdminController {
    private final AuthenticateAdminUseCase authenticateAdminUseCase;
    
    public AdminController(AuthenticateAdminUseCase authenticateAdminUseCase) {
        this.authenticateAdminUseCase = authenticateAdminUseCase;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticateAdminUseCase.execute(request));
    }
}
