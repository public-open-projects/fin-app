package com.socrates.fin_app.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record ChatTransactionRequest(
    @NotBlank(message = "Session ID is required")
    String sessionId,
    
    @NotBlank(message = "Transaction type is required")
    String transactionType,
    
    @NotNull(message = "Transaction details are required")
    Map<String, String> transactionDetails
) {}
