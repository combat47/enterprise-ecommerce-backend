package com.combat47.ecommerce.cart.application.port.in;

import com.combat47.ecommerce.cart.application.command.UpdateCartItemQuantityCommand;
import com.combat47.ecommerce.cart.application.model.CartResponse;

public interface UpdateCartItemQuantityUseCase {

    CartResponse updateCartItemQuantity(UpdateCartItemQuantityCommand command);

}
