package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.CreateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.in.CreateProductUseCase;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.exception.DuplicateSkuException;
import com.combat47.ecommerce.catalog.domain.model.*;
import org.springframework.stereotype.Service;


@Service
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(CreateProductCommand command) {

        ProductName name = new ProductName(command.name());
        Description description = new Description(command.description());
        Price price = new Price(command.price());
        Sku sku = new Sku(command.sku());

        if (productRepository.existsBySku(sku)) {
            throw new DuplicateSkuException("Product with sku " + sku.getValue() + " already exists");
        }

        Product product = Product.create(name, description, price, sku);

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
