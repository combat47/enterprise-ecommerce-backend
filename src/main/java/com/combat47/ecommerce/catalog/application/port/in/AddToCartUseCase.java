package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.AddToCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;

public interface AddToCartUseCase {
    CartResponse addToCart(AddToCartCommand command);
}
