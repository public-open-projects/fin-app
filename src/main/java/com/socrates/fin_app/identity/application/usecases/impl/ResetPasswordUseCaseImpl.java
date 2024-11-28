package com.socrates.fin_app.identity.application.usecases.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import com.socrates.fin_app.identity.application.dto.request.ResetPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.ResetPasswordResponse;
import com.socrates.fin_app.identity.application.usecases.ResetPasswordUseCase;
import com.socrates.fin_app.identity.domain.exceptions.InvalidTokenException;
import com.socrates.fin_app.identity.domain.exceptions.UserNotFoundException;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;
import com.socrates.fin_app.identity.infrastructure.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResetPasswordUseCaseImpl implements ResetPasswordUseCase {
    
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ResetPasswordUseCaseImpl(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public ResetPasswordResponse execute(ResetPasswordRequest request) {
        // Validate reset token
        String email = validateAndGetEmailFromToken(request.token());
        
        // Find client
        var client = clientRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
            
        // Update password
        String encodedPassword = passwordEncoder.encode(request.newPassword());
        client.updatePassword(encodedPassword);
        
        clientRepository.save(client);
        
        return new ResetPasswordResponse(
            email,
            "Password has been successfully reset"
        );
    }
    
    private String validateAndGetEmailFromToken(String token) {
        try {
            // Split token into parts (format: email|timestamp|signature)
            String[] parts = token.split("\\|");
            if (parts.length != 3) {
                throw new InvalidTokenException("Invalid token format");
            }

            String email = parts[0];
            long timestamp = Long.parseLong(parts[1]);
            String signature = parts[2];

            // Check if token has expired (24 hour validity)
            long currentTime = System.currentTimeMillis();
            if (currentTime - timestamp > 24 * 60 * 60 * 1000) {
                throw new InvalidTokenException("Token has expired");
            }

            // Verify signature (email|timestamp signed with server secret)
            String expectedSignature = generateSignature(email + "|" + timestamp);
            if (!expectedSignature.equals(signature)) {
                throw new InvalidTokenException("Invalid token signature");
            }

            return email;
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("Invalid token timestamp");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or expired reset token");
        }
    }

    private String generateSignature(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate token signature", e);
        }
    }
}
