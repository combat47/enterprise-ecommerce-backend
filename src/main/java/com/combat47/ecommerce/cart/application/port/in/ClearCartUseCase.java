package com.combat47.ecommerce.cart.application.port.in;

import com.combat47.ecommerce.cart.application.command.ClearCartCommand;
import com.combat47.ecommerce.cart.application.model.CartResponse;

public interface ClearCartUseCase {

    CartResponse clearCart(ClearCartCommand command);

}
