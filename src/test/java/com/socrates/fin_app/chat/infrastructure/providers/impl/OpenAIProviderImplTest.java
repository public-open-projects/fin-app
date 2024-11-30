package com.socrates.fin_app.chat.infrastructure.providers.impl;

import com.socrates.fin_app.chat.domain.entities.RunStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIProviderImplTest {

    private static final String ASSISTANT_ID = "test-assistant-id";
    private static final String THREAD_ID = "test-thread-id";
    private static final String RUN_ID = "test-run-id";
    private static final String MESSAGE = "test message";

    @Mock
    private RestTemplate restTemplate;

    private OpenAIProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new OpenAIProviderImpl(restTemplate, ASSISTANT_ID);
    }

    @Test
    void createThread_ShouldReturnThreadId() {
        // Given
        Map<String, String> response = Collections.singletonMap("id", THREAD_ID);
        when(restTemplate.postForObject(
            eq("https://api.openai.com/v1/threads"),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(response);

        // When
        String result = provider.createThread();

        // Then
        assertEquals(THREAD_ID, result);
    }

    @Test
    void createMessageAndRun_ShouldReturnRunId() {
        // Given
        String messageUrl = "https://api.openai.com/v1/threads/" + THREAD_ID + "/messages";
        String runUrl = "https://api.openai.com/v1/threads/" + THREAD_ID + "/runs";
        
        Map<String, String> messageResponse = Collections.singletonMap("id", "msg-123");
        Map<String, String> runResponse = Collections.singletonMap("id", RUN_ID);

        when(restTemplate.postForObject(
            eq(messageUrl),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(messageResponse);

        when(restTemplate.postForObject(
            eq(runUrl),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(runResponse);

        // When
        String result = provider.createMessageAndRun(THREAD_ID, MESSAGE);

        // Then
        assertEquals(RUN_ID, result);
    }

    @Test
    void getRunStatus_WhenCompleted_ShouldReturnCompletedStatus() {
        // Given
        String url = "https://api.openai.com/v1/threads/" + THREAD_ID + "/runs/" + RUN_ID;
        Map<String, Object> response = Collections.singletonMap("status", "completed");

        when(restTemplate.getForObject(
            eq(url),
            eq(Map.class)
        )).thenReturn(response);

        // When
        RunStatus result = provider.getRunStatus(THREAD_ID, RUN_ID);

        // Then
        assertEquals(RunStatus.Status.COMPLETED, result.status());
        assertTrue(result.messages().isEmpty());
    }

    @Test
    void getRunStatus_WhenInProgress_ShouldReturnInProgressStatus() {
        // Given
        String url = "https://api.openai.com/v1/threads/" + THREAD_ID + "/runs/" + RUN_ID;
        Map<String, Object> response = Collections.singletonMap("status", "in_progress");

        when(restTemplate.getForObject(
            eq(url),
            eq(Map.class)
        )).thenReturn(response);

        // When
        RunStatus result = provider.getRunStatus(THREAD_ID, RUN_ID);

        // Then
        assertEquals(RunStatus.Status.IN_PROGRESS, result.status());
        assertTrue(result.messages().isEmpty());
    }

    @Test
    void getRunStatus_WhenUnknownStatus_ShouldReturnQueuedStatus() {
        // Given
        String url = "https://api.openai.com/v1/threads/" + THREAD_ID + "/runs/" + RUN_ID;
        Map<String, Object> response = Collections.singletonMap("status", "unknown_status");

        when(restTemplate.getForObject(
            eq(url),
            eq(Map.class)
        )).thenReturn(response);

        // When
        RunStatus result = provider.getRunStatus(THREAD_ID, RUN_ID);

        // Then
        assertEquals(RunStatus.Status.QUEUED, result.status());
        assertTrue(result.messages().isEmpty());
    }
}
