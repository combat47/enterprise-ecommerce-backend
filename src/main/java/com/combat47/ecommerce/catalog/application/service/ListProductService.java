package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.in.ListProductsUseCase;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ListProductService implements ListProductsUseCase {

    private final ProductRepository productRepository;

    public ListProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<ProductResponse> listActiveProducts() {
        return productRepository.findAllActive()
                .stream()
                .map(this::toResponse)
                .toList();
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
