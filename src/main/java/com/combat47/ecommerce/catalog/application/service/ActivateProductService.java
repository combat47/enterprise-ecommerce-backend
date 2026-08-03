package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.ActiveProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.in.ActivateProductUseCase;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.Product;
import org.springframework.stereotype.Service;


@Service
public class ActivateProductService implements ActivateProductUseCase {

    private final ProductRepository productRepository;

    public ActivateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse activate(ActiveProductCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + command.productId()));

        product.activate();

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName().getValue(),
                savedProduct.getDescription() != null ? savedProduct.getDescription().getValue() : null,
                savedProduct.getPrice().getValue(),
                savedProduct.getSku().getValue(),
                savedProduct.isActive()
        );
    }
}
