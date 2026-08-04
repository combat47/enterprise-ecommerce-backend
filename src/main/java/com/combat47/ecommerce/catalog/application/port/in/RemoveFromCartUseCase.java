package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.RemoveFromCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;

public interface RemoveFromUseCase {
    CartResponse removeFromCart(RemoveFromCartCommand command);
}
