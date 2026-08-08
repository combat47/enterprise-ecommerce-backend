package com.combat47.ecommerce.cart.application.port.in;

import com.combat47.ecommerce.cart.application.command.AddToCartCommand;
import com.combat47.ecommerce.cart.application.model.CartResponse;

public interface AddToCartUseCase {

    CartResponse addToCart(AddToCartCommand command);

}
