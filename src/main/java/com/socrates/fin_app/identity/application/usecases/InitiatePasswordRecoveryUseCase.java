package com.socrates.fin_app.identity.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.identity.application.dto.request.ForgotPasswordRequest;
import com.socrates.fin_app.identity.application.dto.response.PasswordRecoveryResponse;

public interface InitiatePasswordRecoveryUseCase extends UseCaseCommand<ForgotPasswordRequest, PasswordRecoveryResponse> {
}
