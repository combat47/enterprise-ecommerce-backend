package com.combat47.ecommerce.order.infrastructure.persistence.repository;

import com.combat47.ecommerce.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findAllByUserId(UUID userId);
}
