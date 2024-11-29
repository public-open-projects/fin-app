package com.socrates.fin_app.chat.interfaces;

import com.socrates.fin_app.common.annotations.ApiController;
import com.socrates.fin_app.chat.application.dto.request.CreateMessageRequest;
import com.socrates.fin_app.chat.application.dto.request.InitializeChatRequest;
import com.socrates.fin_app.chat.application.dto.response.CreateMessageResponse;
import com.socrates.fin_app.chat.application.dto.response.InitializeChatResponse;
import com.socrates.fin_app.chat.application.dto.response.RunStatusResponse;
import com.socrates.fin_app.chat.application.usecases.CreateMessageUseCase;
import com.socrates.fin_app.chat.application.usecases.GetRunStatusUseCase;
import com.socrates.fin_app.chat.application.usecases.InitializeChatUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "Chat API endpoints")
@ApiController
@RequestMapping("/api/chat")
public class ChatController {
    private final InitializeChatUseCase initializeChatUseCase;
    private final CreateMessageUseCase createMessageUseCase;
    private final GetRunStatusUseCase getRunStatusUseCase;
    
    public ChatController(
            InitializeChatUseCase initializeChatUseCase,
            CreateMessageUseCase createMessageUseCase,
            GetRunStatusUseCase getRunStatusUseCase) {
        this.initializeChatUseCase = initializeChatUseCase;
        this.createMessageUseCase = createMessageUseCase;
        this.getRunStatusUseCase = getRunStatusUseCase;
    }
    
    @Operation(summary = "Initialize chat thread")
    @PostMapping("/initialize")
    public ResponseEntity<InitializeChatResponse> initializeChat(
            @Valid @RequestBody InitializeChatRequest request) {
        return ResponseEntity.ok(initializeChatUseCase.execute(request));
    }
    
    @Operation(summary = "Create message and start run")
    @PostMapping("/threads/{threadId}/messages")
    public ResponseEntity<CreateMessageResponse> createMessage(
            @PathVariable String threadId,
            @Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.ok(createMessageUseCase.execute(threadId, request));
    }
    
    @Operation(summary = "Get run status")
    @GetMapping("/threads/{threadId}/runs/{runId}")
    public ResponseEntity<RunStatusResponse> getRunStatus(
            @PathVariable String threadId,
            @PathVariable String runId) {
        return ResponseEntity.ok(getRunStatusUseCase.execute(threadId, runId));
    }
}
