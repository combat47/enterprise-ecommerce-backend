package com.combat47.ecommerce.order.application.service;

import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.ListOrdersUseCase;
import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ListOrdersService implements ListOrdersUseCase {

    private final OrderRepository orderRepository;

    public ListOrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderResponse> listOrders(UUID userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber().getValue(),
                order.getUserId(),
                order.getItems().stream()
                        .map(item -> new OrderResponse.OrderItemResponse(
                                item.getProductId(),
                                item.getProductName(),
                                item.getUnitPrice().getAmount(),
                                item.getQuantity(),
                                item.getTotalPrice().getAmount()
                        ))
                        .toList(),
                order.getStatus().name(),
                order.getTotalAmount().getAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
