package com.combat47.ecommerce.order.application.port.in;

import com.combat47.ecommerce.order.application.command.GetOrderQuery;
import com.combat47.ecommerce.order.application.model.OrderResponse;

public interface GetOrderUseCase {
    OrderResponse getOrder(GetOrderQuery query);
}
