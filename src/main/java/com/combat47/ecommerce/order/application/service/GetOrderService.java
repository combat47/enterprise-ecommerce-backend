package com.combat47.ecommerce.order.application.service;

import com.combat47.ecommerce.order.application.command.GetOrderQuery;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.GetOrderUseCase;
import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.domain.exception.OrderNotFoundException;
import com.combat47.ecommerce.order.domain.model.Order;
import org.springframework.stereotype.Service;


@Service
public class GetOrderService implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse getOrder(GetOrderQuery query) {
        Order order = orderRepository.findById(query.orderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!order.getUserId().equals(query.userId())) {
            throw new IllegalArgumentException("User does not have access to this order");
        }

        return toResponse(order);
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
