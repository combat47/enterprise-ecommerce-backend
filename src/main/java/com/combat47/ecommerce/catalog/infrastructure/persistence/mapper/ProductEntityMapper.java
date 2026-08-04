package com.combat47.ecommerce.catalog.infrastructure.persistence.mapper;

import com.combat47.ecommerce.catalog.domain.model.*;
import com.combat47.ecommerce.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;


@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
          product.getId(),
          product.getName().getValue(),
          product.getDescription() != null ? product.getDescription().getValue() : null,
          product.getPrice().getValue(),
          product.getSku().getValue(),
          product.isActive(),
          product.getCreatedAt(),
          product.getUpdatedAt()
        );
    }

    public Product toDomain(ProductEntity entity) {
        return Product.restore(
                entity.getId(),
                new ProductName(entity.getName()),
                entity.getDescription() != null ? new Description(entity.getDescription()) : null,
                new Price(entity.getPrice()),
                new Sku(entity.getSku()),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
