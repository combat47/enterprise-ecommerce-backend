package com.combat47.ecommerce.order.application.port.out;

import com.combat47.ecommerce.order.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAllByUserId(UUID userId);

}
