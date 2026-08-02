package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.CreateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;

public interface CreateProductUseCase {
    ProductResponse create(CreateProductCommand command);
}
