package com.socrates.fin_app.identity.application.usecases;

import com.socrates.fin_app.common.usecases.UseCaseCommand;
import com.socrates.fin_app.identity.application.dto.request.UpdateProfileRequest;
import com.socrates.fin_app.identity.application.dto.response.ProfileResponse;

public interface UpdateClientProfileUseCase extends UseCaseCommand<UpdateProfileRequest, ProfileResponse> {
    ProfileResponse execute(String clientId, UpdateProfileRequest request);
}
