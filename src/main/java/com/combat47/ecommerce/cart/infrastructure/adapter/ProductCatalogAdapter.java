package com.combat47.ecommerce.cart.infrastructure.adapter;

import com.combat47.ecommerce.cart.application.model.ProductSnapshot;
import com.combat47.ecommerce.cart.application.port.out.ProductCatalogPort;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
public class ProductCatalogAdapter implements ProductCatalogPort {

    private final ProductRepository productRepository;

    public ProductCatalogAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductSnapshot> findById(UUID productId) {
        return productRepository.findById(productId)
                .map(this::toSnapshot);
    }

    private ProductSnapshot toSnapshot(Product product) {
        return new ProductSnapshot(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue(),
                product.isActive()
        );
    }
}
