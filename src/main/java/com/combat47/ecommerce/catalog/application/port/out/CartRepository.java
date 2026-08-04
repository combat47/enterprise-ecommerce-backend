package com.combat47.ecommerce.catalog.application.port.out;

import com.combat47.ecommerce.catalog.domain.model.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Cart save(Cart cart);
    Optional<Cart> findByUserId(UUID userId);
    void delete(Cart cart);
}
