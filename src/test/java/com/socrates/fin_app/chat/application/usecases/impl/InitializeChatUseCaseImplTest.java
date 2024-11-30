package com.socrates.fin_app.chat.application.usecases.impl;

import com.socrates.fin_app.chat.application.dto.request.InitializeChatRequest;
import com.socrates.fin_app.chat.application.dto.response.InitializeChatResponse;
import com.socrates.fin_app.chat.infrastructure.providers.OpenAIProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InitializeChatUseCaseImplTest {

    @Mock
    private OpenAIProvider openAIProvider;

    private InitializeChatUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new InitializeChatUseCaseImpl(openAIProvider);
    }

    @Test
    void whenAuthenticatedUser_thenReturnAuthenticatedWelcomeMessage() {
        // Given
        String threadId = "test-thread-id";
        when(openAIProvider.createThread()).thenReturn(threadId);
        
        InitializeChatRequest request = new InitializeChatRequest("test-id", true);

        // When
        InitializeChatResponse response = useCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(threadId, response.sessionId());
        assertEquals("Welcome back! How can I assist you today?", response.welcomeMessage());
        verify(openAIProvider).createThread();
    }

    @Test
    void whenUnauthenticatedUser_thenReturnGeneralWelcomeMessage() {
        // Given
        String threadId = "test-thread-id";
        when(openAIProvider.createThread()).thenReturn(threadId);
        
        InitializeChatRequest request = new InitializeChatRequest("test-id", false);

        // When
        InitializeChatResponse response = useCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(threadId, response.sessionId());
        assertEquals("Hello! How can I help you?", response.welcomeMessage());
        verify(openAIProvider).createThread();
    }
}
