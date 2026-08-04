package com.combat47.ecommerce.catalog.infrastructure.persistence.repository;

import com.combat47.ecommerce.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findBySku(String sku);

    List<ProductEntity> findAllByActiveTrue();

    boolean existsBySku(String sku);
}
