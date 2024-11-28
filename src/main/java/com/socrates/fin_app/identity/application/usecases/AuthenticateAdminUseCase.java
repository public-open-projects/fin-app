package com.socrates.fin_app.identity.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.identity.application.dto.request.LoginRequest;
import com.socrates.fin_app.identity.application.dto.response.AuthenticationResponse;

public interface AuthenticateAdminUseCase extends UseCaseCommand<LoginRequest, AuthenticationResponse> {
}
