package com.combat47.ecommerce.identity.application.port.in;

import com.combat47.ecommerce.identity.application.command.RefreshTokenCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;

public interface RefreshTokenUseCase {

    TokenResponse refreshToken(RefreshTokenCommand command);
}
