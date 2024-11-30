package com.socrates.fin_app.chat.domain.entities;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RunStatusTest {

    @Test
    void constructor_WithValidParameters_ShouldCreateRunStatus() {
        // Given
        RunStatus.Status status = RunStatus.Status.IN_PROGRESS;
        List<String> messages = Arrays.asList("Message 1", "Message 2");

        // When
        RunStatus runStatus = new RunStatus(status, messages);

        // Then
        assertEquals(status, runStatus.status());
        assertEquals(messages, runStatus.messages());
    }

    @Test
    void constructor_WithEmptyMessages_ShouldCreateRunStatus() {
        // Given
        RunStatus.Status status = RunStatus.Status.COMPLETED;
        List<String> messages = Collections.emptyList();

        // When
        RunStatus runStatus = new RunStatus(status, messages);

        // Then
        assertEquals(status, runStatus.status());
        assertTrue(runStatus.messages().isEmpty());
    }

    @Test
    void status_ShouldHaveAllRequiredValues() {
        // Verify all expected status values exist
        List<RunStatus.Status> expectedStatuses = Arrays.asList(
            RunStatus.Status.QUEUED,
            RunStatus.Status.IN_PROGRESS,
            RunStatus.Status.COMPLETED,
            RunStatus.Status.FAILED,
            RunStatus.Status.CANCELLED,
            RunStatus.Status.EXPIRED
        );

        // When & Then
        assertEquals(6, RunStatus.Status.values().length);
        assertTrue(Arrays.asList(RunStatus.Status.values()).containsAll(expectedStatuses));
    }
}
