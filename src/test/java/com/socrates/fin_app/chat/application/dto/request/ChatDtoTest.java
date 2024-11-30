package com.socrates.fin_app.chat.application.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatDtoTest {

    @Test
    void initializeChatRequest_ShouldCreateWithValidParameters() {
        // Given
        String identifier = "test-id";
        boolean authenticated = true;

        // When
        InitializeChatRequest request = new InitializeChatRequest(identifier, authenticated);

        // Then
        assertEquals(identifier, request.identifier());
        assertEquals(authenticated, request.authenticated());
    }

    @Test
    void createMessageRequest_ShouldCreateWithValidParameters() {
        // Given
        String message = "Hello, world!";

        // When
        CreateMessageRequest request = new CreateMessageRequest(message);

        // Then
        assertEquals(message, request.message());
    }

    @Test
    void chatCommandRequest_ShouldCreateWithValidParameters() {
        // Given
        String sessionId = "session-123";
        String command = "help";

        // When
        ChatCommandRequest request = new ChatCommandRequest(sessionId, command);

        // Then
        assertEquals(sessionId, request.sessionId());
        assertEquals(command, request.command());
    }
}
