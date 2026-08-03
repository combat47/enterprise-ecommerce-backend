package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.UpdateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;

public interface UpdateProductUseCase {
    ProductResponse update(UpdateProductCommand command);
}
