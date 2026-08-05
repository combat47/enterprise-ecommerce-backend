package com.combat47.ecommerce.order.infrastructure.persistence.mapper;

import com.combat47.ecommerce.order.domain.model.*;
import com.combat47.ecommerce.order.infrastructure.persistence.entity.OrderEntity;
import com.combat47.ecommerce.order.infrastructure.persistence.entity.OrderItemEntity;
import com.combat47.ecommerce.order.infrastructure.persistence.entity.OrderStatusEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class OrderEntityMapper {

    public OrderEntity toEntity(Order domain) {
        OrderEntity entity = new OrderEntity();
        entity.setId(domain.getId());
        entity.setOrderNumber(domain.getOrderNumber().getValue());
        entity.setStatus(OrderStatusEntity.valueOf(domain.getStatus().name()));
        entity.setTotalAmount(domain.getTotalAmount().getAmount());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        var items = domain.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .collect(Collectors.toList());
        entity.setItems(items);

        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        var items = entity.getItems().stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());

        return Order.restore(
                entity.getId(),
                new OrderNumber(entity.getOrderNumber()),
                entity.getUserId(),
                items,
                OrderStatus.valueOf(entity.getStatus().name()),
                new Money(entity.getTotalAmount()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()

        );
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity order) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setOrder(order);
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setUnitPrice(item.getUnitPrice().getAmount());
        entity.setQuantity(item.getQuantity());
        entity.setTotalPrice(item.getTotalPrice().getAmount());

        return entity;
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        return OrderItem.restore(
                entity.getId(),
                entity.getProductId(),
                entity.getProductName(),
                new Money(entity.getUnitPrice()),
                entity.getQuantity(),
                new Money(entity.getTotalPrice())
        );
    }

}
