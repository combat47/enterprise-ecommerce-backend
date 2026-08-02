package com.combat47.ecommerce.catalog.infrastructure.persistence.inmemory;


import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.model.Product;
import com.combat47.ecommerce.catalog.domain.model.Sku;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> store = new ConcurrentHashMap<>();
    private final Map<String, UUID> skuIndex = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        store.put(product.getId(), product);
        skuIndex.put(product.getSku().getValue(), product.getId());
        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Product> findBySku(Sku sku) {
        UUID id =  skuIndex.get(sku.getValue());
        if (id == null) return Optional.empty();
        return findById(id);
    }

    @Override
    public boolean existsBySku(Sku sku) {
        return skuIndex.containsKey(sku.getValue());
    }
}
