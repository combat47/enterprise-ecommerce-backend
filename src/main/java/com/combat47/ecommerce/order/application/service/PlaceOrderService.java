package com.combat47.ecommerce.order.application.service;

import com.combat47.ecommerce.order.application.command.PlaceOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.PlaceOrderUseCase;
import com.combat47.ecommerce.order.application.port.out.InventoryPort;
import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.domain.model.Money;
import com.combat47.ecommerce.order.domain.model.Order;
import com.combat47.ecommerce.order.domain.model.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final InventoryPort inventoryPort;

    public PlaceOrderService(OrderRepository orderRepository, InventoryPort inventoryPort) {
        this.orderRepository = orderRepository;
        this.inventoryPort = inventoryPort;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderCommand command) {
        for (PlaceOrderCommand.OrderItemCommand item : command.items()) {
            if (!inventoryPort.hasEnoughStock(item.productId(), item.quantity())) {
                throw new IllegalArgumentException("Insufficient stock for product: " + item.productId());
            }
        }

        var items = command.items().stream()
                .map(item -> OrderItem.create(
                        item.productId(),
                        item.productName(),
                        new Money(item.unitPrice()),
                        item.quantity()
                ))
                .toList();
        Order order = Order.create(command.userId(), items);

        for (PlaceOrderCommand.OrderItemCommand item : command.items()) {
            inventoryPort.reserveStock(item.productId(), item.quantity());
        }

        Order saved = orderRepository.save(order);

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
