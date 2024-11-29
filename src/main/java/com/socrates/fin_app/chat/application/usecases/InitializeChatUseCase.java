package com.socrates.fin_app.chat.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.chat.application.dto.request.InitializeChatRequest;
import com.socrates.fin_app.chat.application.dto.response.InitializeChatResponse;

public interface InitializeChatUseCase extends UseCaseCommand<InitializeChatRequest, InitializeChatResponse> {
}
