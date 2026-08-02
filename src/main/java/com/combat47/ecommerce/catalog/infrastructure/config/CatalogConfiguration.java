package com.combat47.ecommerce.catalog.infrastructure.config;

import com.combat47.ecommerce.catalog.application.port.in.CreateProductUseCase;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.application.service.CreateProductService;
import com.combat47.ecommerce.catalog.infrastructure.persistence.inmemory.InMemoryProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CatalogConfiguration {

    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    @Bean
    public CreateProductUseCase  createProductUseCase(ProductRepository productRepository) {
        return new CreateProductService(productRepository);
    }

}
