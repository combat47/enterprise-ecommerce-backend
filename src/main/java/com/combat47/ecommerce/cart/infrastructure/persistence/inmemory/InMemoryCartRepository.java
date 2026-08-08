package com.combat47.ecommerce.cart.infrastructure.persistence.inmemory;

import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.domain.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCartRepository implements CartRepository {

    private final Map<UUID, Cart> store = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> userToCart = new ConcurrentHashMap<>();

    @Override
    public Cart save(Cart cart) {
        store.put(cart.getId(), cart);
        userToCart.put(cart.getCustomerId(), cart.getId());
        return cart;
    }

    @Override
    public Optional<Cart> findByCustomerId(UUID customerId) {
        UUID cartId = userToCart.get(customerId);
        if (cartId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(cartId));
    }

    @Override
    public void delete(Cart cart) {
        store.remove(cart.getId());
        userToCart.remove(cart.getCustomerId());
    }
}