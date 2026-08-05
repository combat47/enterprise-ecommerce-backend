package com.combat47.ecommerce.order.infrastructure.persistence.adapter;

import com.combat47.ecommerce.order.application.port.out.OrderRepository;
import com.combat47.ecommerce.order.domain.model.Order;
import com.combat47.ecommerce.order.infrastructure.persistence.entity.OrderEntity;
import com.combat47.ecommerce.order.infrastructure.persistence.mapper.OrderEntityMapper;
import com.combat47.ecommerce.order.infrastructure.persistence.repository.JpaOrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class JpaOrderRepositoryAdapter implements OrderRepository {
    
    private final JpaOrderRepository jpaRepository;
    private final OrderEntityMapper mapper;

    public JpaOrderRepositoryAdapter(JpaOrderRepository jpaRepository, OrderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(mapper::toDomain).toList();
    }
}
