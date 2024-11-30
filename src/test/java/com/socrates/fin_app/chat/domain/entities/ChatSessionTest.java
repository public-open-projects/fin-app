package com.socrates.fin_app.chat.domain.entities;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ChatSessionTest {

    @Test
    void whenCreatingAuthenticatedSession_thenPropertiesAreCorrectlySet() {
        // Given
        String userId = "test-user-id";

        // When
        ChatSession session = new ChatSession(userId);

        // Then
        assertNotNull(session.getId(), "Session ID should not be null");
        assertEquals(userId, session.getUserId(), "User ID should match");
        assertNull(session.getGuestId(), "Guest ID should be null for authenticated session");
        assertTrue(session.isAuthenticated(), "Session should be authenticated");
        assertEquals("ACTIVE", session.getStatus(), "Status should be ACTIVE");
        assertNotNull(session.getStartTime(), "Start time should not be null");
        assertTrue(session.getStartTime().isBefore(LocalDateTime.now().plusSeconds(1)), 
                  "Start time should be before or equal to now");
        assertNull(session.getEndTime(), "End time should be null for new session");
    }

    @Test
    void whenCreatingUnauthenticatedSession_thenPropertiesAreCorrectlySet() {
        // Given
        String guestId = "test-guest-id";

        // When
        ChatSession session = new ChatSession(guestId, false);

        // Then
        assertNotNull(session.getId(), "Session ID should not be null");
        assertNull(session.getUserId(), "User ID should be null for unauthenticated session");
        assertEquals(guestId, session.getGuestId(), "Guest ID should match");
        assertFalse(session.isAuthenticated(), "Session should not be authenticated");
        assertEquals("ACTIVE", session.getStatus(), "Status should be ACTIVE");
        assertNotNull(session.getStartTime(), "Start time should not be null");
        assertTrue(session.getStartTime().isBefore(LocalDateTime.now().plusSeconds(1)), 
                  "Start time should be before or equal to now");
        assertNull(session.getEndTime(), "End time should be null for new session");
    }

    @Test
    void whenUsingDefaultConstructor_thenEntityIsCreated() {
        // When
        ChatSession session = new ChatSession();

        // Then
        assertNotNull(session, "Session should be created");
    }
}
