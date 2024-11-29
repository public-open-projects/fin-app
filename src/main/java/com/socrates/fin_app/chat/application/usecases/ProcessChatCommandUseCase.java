package com.socrates.fin_app.chat.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.chat.application.dto.request.ChatCommandRequest;
import com.socrates.fin_app.chat.application.dto.response.ChatCommandResponse;

public interface ProcessChatCommandUseCase extends UseCaseCommand<ChatCommandRequest, ChatCommandResponse> {
}
