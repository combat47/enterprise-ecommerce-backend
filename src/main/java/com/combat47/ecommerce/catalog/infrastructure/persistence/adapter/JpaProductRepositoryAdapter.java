package com.combat47.ecommerce.catalog.infrastructure.persistence.adapter;

import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.model.Product;
import com.combat47.ecommerce.catalog.domain.model.Sku;
import com.combat47.ecommerce.catalog.infrastructure.persistence.entity.ProductEntity;
import com.combat47.ecommerce.catalog.infrastructure.persistence.mapper.ProductEntityMapper;
import com.combat47.ecommerce.catalog.infrastructure.persistence.repository.JpaProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductEntityMapper mapper;

    public JpaProductRepositoryAdapter(JpaProductRepository jpaProductRepository, ProductEntityMapper mapper) {
        this.jpaProductRepository = jpaProductRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity productEntity = mapper.toEntity(product);
        ProductEntity saved = jpaProductRepository.save(productEntity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findBySku(Sku sku) {
        return jpaProductRepository.findBySku(sku.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllActive() {
        return jpaProductRepository.findAllByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(Sku sku) {
        return jpaProductRepository.existsBySku(sku.getValue());
    }
}
