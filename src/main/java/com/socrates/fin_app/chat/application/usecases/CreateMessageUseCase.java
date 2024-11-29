package com.socrates.fin_app.chat.application.usecases;

import com.socrates.fin_app.chat.application.dto.request.CreateMessageRequest;
import com.socrates.fin_app.chat.application.dto.response.CreateMessageResponse;

public interface CreateMessageUseCase {
    CreateMessageResponse execute(String threadId, CreateMessageRequest request);
}
