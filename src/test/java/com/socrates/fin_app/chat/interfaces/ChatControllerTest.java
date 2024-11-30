package com.socrates.fin_app.chat.interfaces;

import com.socrates.fin_app.chat.application.dto.request.CreateMessageRequest;
import com.socrates.fin_app.chat.application.dto.request.InitializeChatRequest;
import com.socrates.fin_app.chat.application.dto.response.CreateMessageResponse;
import com.socrates.fin_app.chat.application.dto.response.InitializeChatResponse;
import com.socrates.fin_app.chat.application.dto.response.RunStatusResponse;
import com.socrates.fin_app.chat.application.usecases.CreateMessageUseCase;
import com.socrates.fin_app.chat.application.usecases.GetRunStatusUseCase;
import com.socrates.fin_app.chat.application.usecases.InitializeChatUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
@Import(ChatTestSecurityConfig.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InitializeChatUseCase initializeChatUseCase;

    @MockBean
    private CreateMessageUseCase createMessageUseCase;

    @MockBean
    private GetRunStatusUseCase getRunStatusUseCase;

    @Test
    void initializeChat_Success() throws Exception {
        // Given
        InitializeChatResponse response = new InitializeChatResponse(
            "test-thread-id",
            "Welcome!"
        );
        when(initializeChatUseCase.execute(any(InitializeChatRequest.class)))
            .thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/chat/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"identifier\":\"test-id\",\"authenticated\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("test-thread-id"))
                .andExpect(jsonPath("$.welcomeMessage").value("Welcome!"));
    }

    @Test
    void createMessage_Success() throws Exception {
        // Given
        CreateMessageResponse response = new CreateMessageResponse(
            "test-thread-id",
            "test-run-id",
            "started"
        );
        when(createMessageUseCase.execute(any(), any(CreateMessageRequest.class)))
            .thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/chat/threads/test-thread-id/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.threadId").value("test-thread-id"))
                .andExpect(jsonPath("$.runId").value("test-run-id"))
                .andExpect(jsonPath("$.status").value("started"));
    }

    @Test
    void getRunStatus_Success() throws Exception {
        // Given
        RunStatusResponse response = new RunStatusResponse(
            "test-thread-id",
            "test-run-id",
            "COMPLETED",
            List.of("Hello!", "How can I help?")
        );
        when(getRunStatusUseCase.execute("test-thread-id", "test-run-id"))
            .thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/chat/threads/test-thread-id/runs/test-run-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.threadId").value("test-thread-id"))
                .andExpect(jsonPath("$.runId").value("test-run-id"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.messages[0]").value("Hello!"))
                .andExpect(jsonPath("$.messages[1]").value("How can I help?"));
    }
}
