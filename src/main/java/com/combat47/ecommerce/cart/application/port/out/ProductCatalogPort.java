package com.combat47.ecommerce.cart.application.port.out;

import com.combat47.ecommerce.cart.application.model.ProductSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface ProductCatalogPort {

    Optional<ProductSnapshot> findById(UUID productId);

}
