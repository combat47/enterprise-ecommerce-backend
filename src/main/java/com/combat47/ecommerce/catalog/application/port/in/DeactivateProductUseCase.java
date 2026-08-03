package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.DeactivateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;

public interface DeactivateProductUseCase {
    ProductResponse deactivate(DeactivateProductCommand command);
}
