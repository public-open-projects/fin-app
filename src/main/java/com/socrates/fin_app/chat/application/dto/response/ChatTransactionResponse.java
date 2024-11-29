package com.socrates.fin_app.chat.application.dto.response;

public record ChatTransactionResponse(
    String sessionId,
    String transactionId,
    String status,
    String message
) {}
