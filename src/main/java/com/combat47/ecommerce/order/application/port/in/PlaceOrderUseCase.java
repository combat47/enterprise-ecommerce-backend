package com.combat47.ecommerce.order.application.port.in;

import com.combat47.ecommerce.order.application.command.PlaceOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;

public interface PlaceOrderUseCase {
    OrderResponse placeOrder(PlaceOrderCommand command);
}
