package com.socrates.fin_app.chat.domain.entities;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ChatSessionTest {

    @Test
    void authenticatedConstructor_ShouldCreateAuthenticatedSession() {
        // Given
        String userId = "testUser";

        // When
        ChatSession session = new ChatSession(userId);

        // Then
        assertNotNull(session.getId());
        assertEquals(userId, session.getUserId());
        assertNull(session.getGuestId());
        assertTrue(session.isAuthenticated());
        assertEquals("ACTIVE", session.getStatus());
        assertNotNull(session.getStartTime());
        assertNull(session.getEndTime());
    }

    @Test
    void unauthenticatedConstructor_ShouldCreateUnauthenticatedSession() {
        // Given
        String guestId = "guest123";

        // When
        ChatSession session = new ChatSession(guestId, false);

        // Then
        assertNotNull(session.getId());
        assertNull(session.getUserId());
        assertEquals(guestId, session.getGuestId());
        assertFalse(session.isAuthenticated());
        assertEquals("ACTIVE", session.getStatus());
        assertNotNull(session.getStartTime());
        assertNull(session.getEndTime());
    }

    @Test
    void defaultConstructor_ShouldExist() {
        // This test verifies the existence of the protected default constructor
        // Required by JPA
        assertDoesNotThrow(() -> ChatSession.class.getDeclaredConstructor());
    }

    @Test
    void getters_ShouldReturnCorrectValues() {
        // Given
        String userId = "testUser";
        ChatSession session = new ChatSession(userId);
        LocalDateTime now = LocalDateTime.now();

        // When & Then
        assertNotNull(session.getId());
        assertEquals(userId, session.getUserId());
        assertEquals("ACTIVE", session.getStatus());
        assertTrue(session.isAuthenticated());
        assertTrue(session.getStartTime().isAfter(now.minusSeconds(1)));
        assertTrue(session.getStartTime().isBefore(now.plusSeconds(1)));
    }
}
