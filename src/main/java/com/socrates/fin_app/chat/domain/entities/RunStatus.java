package com.socrates.fin_app.chat.domain.entities;

import java.util.List;

public record RunStatus(
    String status,
    List<String> messages
) {}
