package com.combat47.ecommerce.cart.application.port.in;

import com.combat47.ecommerce.cart.application.command.GetCartCommand;
import com.combat47.ecommerce.cart.application.model.CartResponse;

public interface GetCartUseCase {

    CartResponse getCart(GetCartCommand command);

}
