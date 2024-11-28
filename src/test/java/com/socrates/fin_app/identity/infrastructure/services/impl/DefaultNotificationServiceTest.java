package com.socrates.fin_app.identity.infrastructure.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class DefaultNotificationServiceTest {

    private DefaultNotificationService notificationService;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        notificationService = new DefaultNotificationService();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void whenSendingPasswordRecoveryEmail_thenCorrectMessageIsLogged() {
        // Given
        String email = "test@example.com";
        String recoveryToken = "recovery-token-123";

        // When
        notificationService.sendPasswordRecoveryEmail(email, recoveryToken);

        // Then
        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Sending password recovery email to: " + email));
        assertTrue(output.contains("Recovery token: " + recoveryToken));
    }
}
