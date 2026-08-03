package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.model.ProductResponse;

import java.util.List;

public interface ListProductsUseCase {
    List<ProductResponse> listActiveProducts();
}
