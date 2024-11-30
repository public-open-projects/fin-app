package com.socrates.fin_app.chat.application.usecases.impl;

import com.socrates.fin_app.chat.application.dto.response.RunStatusResponse;
import com.socrates.fin_app.chat.domain.entities.RunStatus;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRunStatusUseCaseImplTest {

    @Mock
    private OpenAIProvider openAIProvider;

    private GetRunStatusUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetRunStatusUseCaseImpl(openAIProvider);
    }

    @Test
    void whenGettingRunStatus_thenReturnResponse() {
        // Given
        String threadId = "test-thread-id";
        String runId = "test-run-id";
        List<String> messages = List.of("Hello!", "How can I help?");
        
        RunStatus status = new RunStatus(RunStatus.Status.COMPLETED, messages);
        when(openAIProvider.getRunStatus(threadId, runId)).thenReturn(status);

        // When
        RunStatusResponse response = useCase.execute(threadId, runId);

        // Then
        assertNotNull(response);
        assertEquals(threadId, response.threadId());
        assertEquals(runId, response.runId());
        assertEquals("COMPLETED", response.status());
        assertEquals(messages, response.messages());
        verify(openAIProvider).getRunStatus(threadId, runId);
    }
}
