package com.combat47.ecommerce.catalog.application.port.in;

import com.combat47.ecommerce.catalog.application.command.UpdateQuantityCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;

public interface UpdateQuantityUseCase {
    CartResponse updateQuantity(UpdateQuantityCommand command);
}
