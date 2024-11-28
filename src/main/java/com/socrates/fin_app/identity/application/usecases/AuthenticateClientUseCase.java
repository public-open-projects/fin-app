package com.socrates.fin_app.identity.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.identity.application.dto.LoginDto;

public interface AuthenticateClientUseCase extends UseCaseCommand<LoginDto> {
}
