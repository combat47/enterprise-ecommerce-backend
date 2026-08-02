package com.combat47.ecommerce.catalog.application.port.out;

import com.combat47.ecommerce.catalog.domain.model.Product;
import com.combat47.ecommerce.catalog.domain.model.Sku;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Optional<Product> findBySku(Sku sku);
    boolean existsBySku(Sku id);

}
