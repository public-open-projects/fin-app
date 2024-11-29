package com.socrates.fin_app.chat.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.chat.application.dto.request.ChatAssistanceRequest;
import com.socrates.fin_app.chat.application.dto.response.ChatAssistanceResponse;

public interface ProvideChatAssistanceUseCase extends UseCaseCommand<ChatAssistanceRequest, ChatAssistanceResponse> {
}
