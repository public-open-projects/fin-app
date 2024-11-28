package com.socrates.fin_app.identity.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.identity.application.dto.request.ResetPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.ResetPasswordResponse;

public interface ResetPasswordUseCase extends UseCaseCommand<ResetPasswordRequest, ResetPasswordResponse> {
}
