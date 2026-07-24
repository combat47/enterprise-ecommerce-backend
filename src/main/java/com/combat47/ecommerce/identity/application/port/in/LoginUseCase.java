package com.combat47.ecommerce.identity.application.port.in;

import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;

public interface LoginUseCase {

    TokenResponse login(LoginCommand loginCommand);
}
