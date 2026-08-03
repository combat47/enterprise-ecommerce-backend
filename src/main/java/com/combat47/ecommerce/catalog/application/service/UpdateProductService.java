package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.UpdateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.in.UpdateProductUseCase;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.*;
import org.springframework.stereotype.Service;

@Service
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse update(UpdateProductCommand command) {
        Sku sku = new Sku(command.sku());
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + command.sku()));

        ProductName name = new ProductName(command.name());
        Description description = new Description(command.description());
        Price  price = new Price(command.price());

        product.update(name, description, price, sku);

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
