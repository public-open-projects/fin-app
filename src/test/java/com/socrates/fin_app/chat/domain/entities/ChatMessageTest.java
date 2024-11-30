package com.socrates.fin_app.chat.domain.entities;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {

    @Test
    void constructor_WithValidParameters_ShouldCreateMessage() {
        // Given
        ChatSession session = new ChatSession("testUser");
        String content = "Hello, world!";
        String senderType = "USER";

        // When
        ChatMessage message = new ChatMessage(session, content, senderType);

        // Then
        assertNotNull(message.getId());
        assertEquals(session, message.getSession());
        assertEquals(content, message.getContent());
        assertEquals(senderType, message.getSenderType());
        assertNotNull(message.getTimestamp());
        assertTrue(message.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(message.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void getters_ShouldReturnCorrectValues() {
        // Given
        ChatSession session = new ChatSession("testUser");
        String content = "Test message";
        String senderType = "SYSTEM";
        ChatMessage message = new ChatMessage(session, content, senderType);

        // When & Then
        assertNotNull(message.getId());
        assertEquals(session, message.getSession());
        assertEquals(content, message.getContent());
        assertEquals(senderType, message.getSenderType());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void defaultConstructor_ShouldExist() {
        // This test verifies the existence of the protected default constructor
        // Required by JPA
        assertDoesNotThrow(() -> ChatMessage.class.getDeclaredConstructor());
    }
}
