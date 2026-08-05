package com.combat47.ecommerce.order.application.service;

import com.combat47.ecommerce.order.application.command.CancelOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.CancelOrderUseCase;
import com.combat47.ecommerce.order.application.port.out.InventoryPort;
import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final InventoryPort inventoryPort;

    public CancelOrderService(OrderRepository orderRepository, InventoryPort inventoryPort) {
        this.orderRepository = orderRepository;
        this.inventoryPort = inventoryPort;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(CancelOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order Not Found"));

        if (!order.getUserId().equals(command.userId())) {
            throw new IllegalArgumentException("User does not have access to this order");
        }

        order.cancel();

        for (var item : order.getItems()) {
            inventoryPort.releaseStock(item.getProductId(), item.getQuantity());
        }

        Order saved =  orderRepository.save(order);

        return toResponse(saved);
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
