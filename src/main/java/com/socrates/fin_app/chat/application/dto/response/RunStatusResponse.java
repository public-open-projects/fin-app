package com.socrates.fin_app.chat.application.dto.response;

import java.util.List;

public record RunStatusResponse(
    String threadId,
    String runId,
    String status,
    List<String> messages
) {}
