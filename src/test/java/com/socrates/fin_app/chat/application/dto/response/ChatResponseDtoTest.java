package com.socrates.fin_app.chat.application.dto.response;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ChatResponseDtoTest {

    @Test
    void initializeChatResponse_ShouldCreateWithValidParameters() {
        // Given
        String sessionId = "session-123";
        String welcomeMessage = "Welcome to chat!";

        // When
        InitializeChatResponse response = new InitializeChatResponse(sessionId, welcomeMessage);

        // Then
        assertEquals(sessionId, response.sessionId());
        assertEquals(welcomeMessage, response.welcomeMessage());
    }

    @Test
    void createMessageResponse_ShouldCreateWithValidParameters() {
        // Given
        String threadId = "thread-123";
        String runId = "run-456";
        String status = "started";

        // When
        CreateMessageResponse response = new CreateMessageResponse(threadId, runId, status);

        // Then
        assertEquals(threadId, response.threadId());
        assertEquals(runId, response.runId());
        assertEquals(status, response.status());
    }

    @Test
    void runStatusResponse_ShouldCreateWithValidParameters() {
        // Given
        String threadId = "thread-123";
        String runId = "run-456";
        String status = "completed";
        List<String> messages = Arrays.asList("Message 1", "Message 2");

        // When
        RunStatusResponse response = new RunStatusResponse(threadId, runId, status, messages);

        // Then
        assertEquals(threadId, response.threadId());
        assertEquals(runId, response.runId());
        assertEquals(status, response.status());
        assertEquals(messages, response.messages());
    }

    @Test
    void chatAssistanceResponse_ShouldCreateWithValidParameters() {
        // Given
        String sessionId = "session-123";
        String response = "How can I help you?";
        boolean isLiveAgent = false;
        List<String> suggestedActions = Arrays.asList("Action 1", "Action 2");

        // When
        ChatAssistanceResponse chatResponse = new ChatAssistanceResponse(
            sessionId, response, isLiveAgent, suggestedActions);

        // Then
        assertEquals(sessionId, chatResponse.sessionId());
        assertEquals(response, chatResponse.response());
        assertEquals(isLiveAgent, chatResponse.isLiveAgent());
        assertEquals(suggestedActions, chatResponse.suggestedActions());
    }
}
