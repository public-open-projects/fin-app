package com.socrates.fin_app.identity.interfaces;

import com.socrates.fin_app.common.annotations.ApiController;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;
import com.socrates.fin_app.identity.application.usecases.AuthenticateBankerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/api/bankers")
public class BankerController {
    private final AuthenticateBankerUseCase authenticateBankerUseCase;
    
    public BankerController(AuthenticateBankerUseCase authenticateBankerUseCase) {
        this.authenticateBankerUseCase = authenticateBankerUseCase;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticateBankerUseCase.execute(request));
    }
}
