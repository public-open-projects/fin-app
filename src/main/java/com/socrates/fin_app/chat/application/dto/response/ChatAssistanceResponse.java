package com.socrates.fin_app.chat.application.dto.response;

import java.util.List;

public record ChatAssistanceResponse(
    String sessionId,
    String response,
    boolean isLiveAgent,
    List<String> suggestedActions
) {}
