package com.combat47.ecommerce.cart.application.port.out;

import com.combat47.ecommerce.cart.domain.model.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {

    Cart save(Cart cart);

    Optional<Cart> findByCustomerId(UUID id);

    void delete(Cart cart);

}
