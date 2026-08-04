package com.combat47.ecommerce.catalog.infrastructure.config;

import com.combat47.ecommerce.catalog.application.port.in.*;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.application.service.*;
import com.combat47.ecommerce.catalog.infrastructure.persistence.adapter.JpaProductRepositoryAdapter;
import com.combat47.ecommerce.catalog.infrastructure.persistence.mapper.ProductEntityMapper;
import com.combat47.ecommerce.catalog.infrastructure.persistence.repository.JpaProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CatalogConfiguration {

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository, ProductEntityMapper mapper) {
        return new JpaProductRepositoryAdapter(jpaProductRepository, mapper);
    }

    @Bean
    public CreateProductUseCase  createProductUseCase(ProductRepository productRepository) {
        return new CreateProductService(productRepository);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepository productRepository) {
        return new UpdateProductService(productRepository);
    }

    @Bean
    public ActivateProductUseCase  activateProductUseCase(ProductRepository productRepository) {
        return new ActivateProductService(productRepository);
    }

    @Bean
    public DeactivateProductUseCase deactivateProductUseCase(ProductRepository productRepository) {
        return new DeactivateProductService(productRepository);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepository productRepository) {
        return new GetProductService(productRepository);
    }

    @Bean
    public ListProductsUseCase listProductsUseCase(ProductRepository productRepository) {
        return new ListProductService(productRepository);
    }

}
