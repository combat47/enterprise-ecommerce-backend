package com.combat47.ecommerce.order.application.port.in;

import com.combat47.ecommerce.order.application.model.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface ListOrdersUseCase {
    List<OrderResponse> listOrders(UUID userId);
}
