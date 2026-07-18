package com.combat47.ecommerce.identity.application.port.in;

import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;

public interface RegisterUserUseCase {
    void execute(RegisterUserCommand command);
}
