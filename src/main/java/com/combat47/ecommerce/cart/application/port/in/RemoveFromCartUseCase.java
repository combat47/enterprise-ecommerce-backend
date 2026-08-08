package com.combat47.ecommerce.cart.application.port.in;

import com.combat47.ecommerce.cart.application.command.RemoveFromCartCommand;
import com.combat47.ecommerce.cart.application.model.CartResponse;

public interface RemoveFromCartUseCase {

    CartResponse removeFromCart(RemoveFromCartCommand command);

}
