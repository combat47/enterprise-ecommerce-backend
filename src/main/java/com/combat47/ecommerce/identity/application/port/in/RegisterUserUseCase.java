package com.combat47.ecommerce.identity.application.port.in;

import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.domain.model.User;

public interface RegisterUserUseCase {
    User execute(RegisterUserCommand command);
}
