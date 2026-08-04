package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.GetCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;

public interface GetCartUseCase {
    CartResponse getCart(GetCartCommand command);
}
