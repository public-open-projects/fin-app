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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIProviderImplTest {

    private OpenAIProviderImpl openAIProvider;

    @Mock
    private RestTemplate restTemplate;

    private static final String ASSISTANT_ID = "test-assistant-id";
    private static final String THREAD_ID = "test-thread-id";
    private static final String RUN_ID = "test-run-id";
    private static final String TEST_MESSAGE = "test message";

    @BeforeEach
    void setUp() {
        openAIProvider = new OpenAIProviderImpl(restTemplate, ASSISTANT_ID);
    }

    @Test
    void createThread_Success() {
        // Given
        Map<String, String> response = Map.of("id", THREAD_ID);
        when(restTemplate.postForObject(
            contains("/threads"),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(response);

        // When
        String result = openAIProvider.createThread();

        // Then
        assertEquals(THREAD_ID, result);
    }

    @Test
    void createMessageAndRun_Success() {
        // Given
        Map<String, String> messageResponse = Map.of("id", "msg-123");
        Map<String, String> runResponse = Map.of("id", RUN_ID);

        when(restTemplate.postForObject(
            contains("/messages"),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(messageResponse);

        when(restTemplate.postForObject(
            contains("/runs"),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(runResponse);

        // When
        String result = openAIProvider.createMessageAndRun(THREAD_ID, TEST_MESSAGE);

        // Then
        assertEquals(RUN_ID, result);
    }

    @Test
    void getRunStatus_Success() {
        // Given
        Map<String, Object> response = Map.of(
            "status", "completed",
            "messages", Collections.emptyList()
        );

        when(restTemplate.getForObject(
            contains("/runs/"),
            eq(Map.class)
        )).thenReturn(response);

        // When
        RunStatus result = openAIProvider.getRunStatus(THREAD_ID, RUN_ID);

        // Then
        assertNotNull(result);
        assertEquals(RunStatus.Status.COMPLETED, result.status());
        assertTrue(result.messages().isEmpty());
    }

    @Test
    void getRunStatus_InProgress() {
        // Given
        Map<String, Object> response = Map.of(
            "status", "in_progress",
            "messages", Collections.emptyList()
        );

        when(restTemplate.getForObject(
            contains("/runs/"),
            eq(Map.class)
        )).thenReturn(response);

        // When
        RunStatus result = openAIProvider.getRunStatus(THREAD_ID, RUN_ID);

        // Then
        assertNotNull(result);
        assertEquals(RunStatus.Status.IN_PROGRESS, result.status());
    }

    @Test
    void getRunStatus_Failed() {
        // Given
        Map<String, Object> response = Map.of(
            "status", "failed",
            "messages", Collections.emptyList()
        );

        when(restTemplate.getForObject(
            contains("/runs/"),
            eq(Map.class)
        )).thenReturn(response);

        // When
        RunStatus result = openAIProvider.getRunStatus(THREAD_ID, RUN_ID);

        // Then
        assertNotNull(result);
        assertEquals(RunStatus.Status.FAILED, result.status());
    }
}
