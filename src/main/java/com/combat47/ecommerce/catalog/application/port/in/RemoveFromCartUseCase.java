package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.RemoveFromCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;

public interface RemoveFromCartUseCase {
    CartResponse removeFromCart(RemoveFromCartCommand command);
}
