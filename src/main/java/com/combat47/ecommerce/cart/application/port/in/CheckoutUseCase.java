package com.combat47.ecommerce.cart.application.port.in;

import com.combat47.ecommerce.cart.application.command.CheckoutCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;

import java.util.UUID;

public interface CheckoutUseCase {
    OrderResponse checkout(CheckoutCommand command);
}
