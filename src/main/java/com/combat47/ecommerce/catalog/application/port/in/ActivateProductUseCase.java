package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.ActiveProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;

public interface ActivateProductUseCase {
    ProductResponse activate(ActiveProductCommand command);
}
