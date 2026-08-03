package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.in.GetProductUseCase;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.Product;
import com.combat47.ecommerce.catalog.domain.model.Sku;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class GetProductService implements GetProductUseCase {

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        return toResponse(product);
    }

    @Override
    public ProductResponse getBySku(String skuValue) {
        Sku sku = new Sku(skuValue);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with sku: " + skuValue));
        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getDescription() != null ? product.getDescription().getValue() : null,
                product.getPrice().getValue(),
                product.getSku().getValue(),
                product.isActive()
        );
    }
}
