package com.socrates.fin_app.chat.application.dto;

import com.socrates.fin_app.chat.application.dto.response.ChatTransactionResponse;
import com.socrates.fin_app.chat.application.dto.response.ChatCommandResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatResponseDtoTest {

    @Test
    void chatTransactionResponse_ShouldCreateWithValidParameters() {
        // Given
        String sessionId = "session-123";
        String transactionId = "tx-456";
        String status = "completed";
        String message = "Transaction processed successfully";

        // When
        ChatTransactionResponse response = new ChatTransactionResponse(
            sessionId, transactionId, status, message);

        // Then
        assertEquals(sessionId, response.sessionId());
        assertEquals(transactionId, response.transactionId());
        assertEquals(status, response.status());
        assertEquals(message, response.message());
    }

    @Test
    void chatCommandResponse_ShouldCreateWithValidParameters() {
        // Given
        String sessionId = "session-123";
        String result = "Command executed successfully";
        String nextStep = "Please confirm the action";

        // When
        ChatCommandResponse response = new ChatCommandResponse(
            sessionId, result, nextStep);

        // Then
        assertEquals(sessionId, response.sessionId());
        assertEquals(result, response.result());
        assertEquals(nextStep, response.nextStep());
    }
}
