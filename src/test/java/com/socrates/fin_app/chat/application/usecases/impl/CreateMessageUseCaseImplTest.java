package com.socrates.fin_app.chat.application.usecases.impl;

import com.socrates.fin_app.chat.application.dto.request.CreateMessageRequest;
import com.socrates.fin_app.chat.application.dto.response.CreateMessageResponse;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateMessageUseCaseImplTest {

    @Mock
    private OpenAIProvider openAIProvider;

    private CreateMessageUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateMessageUseCaseImpl(openAIProvider);
    }

    @Test
    void whenCreatingMessage_thenReturnResponse() {
        // Given
        String threadId = "test-thread-id";
        String runId = "test-run-id";
        String message = "Hello, how are you?";
        
        when(openAIProvider.createMessageAndRun(threadId, message))
            .thenReturn(runId);

        CreateMessageRequest request = new CreateMessageRequest(message);

        // When
        CreateMessageResponse response = useCase.execute(threadId, request);

        // Then
        assertNotNull(response);
        assertEquals(threadId, response.threadId());
        assertEquals(runId, response.runId());
        assertEquals("started", response.status());
        verify(openAIProvider).createMessageAndRun(threadId, message);
    }
}
