package com.socrates.fin_app.chat.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.chat.application.dto.request.ChatTransactionRequest;
import com.socrates.fin_app.chat.application.dto.response.ChatTransactionResponse;

public interface ExecuteChatTransactionUseCase extends UseCaseCommand<ChatTransactionRequest, ChatTransactionResponse> {
}
