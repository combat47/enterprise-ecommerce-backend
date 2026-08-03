package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.model.ProductResponse;

import java.util.UUID;

public interface GetProductUseCase {
    ProductResponse getById(UUID id);
    ProductResponse getBySku(String sku);
}
