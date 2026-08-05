package com.combat47.ecommerce.order.application.port.in;

import com.combat47.ecommerce.order.application.command.CancelOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;

public interface CancelOrderUseCase {
    OrderResponse cancelOrder(CancelOrderCommand command);
}
