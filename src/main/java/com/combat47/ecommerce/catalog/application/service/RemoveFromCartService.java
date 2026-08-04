package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.RemoveFromCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;
import com.combat47.ecommerce.catalog.application.port.in.RemoveFromCartUseCase;
import com.combat47.ecommerce.catalog.application.port.out.CartRepository;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import org.springframework.stereotype.Service;

@Service
public class RemoveFromCartService implements RemoveFromCartUseCase {

    private final CartRepository cartRepository;

    public RemoveFromCartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Override
    public CartResponse removeFromCart(RemoveFromCartCommand command) {
        Cart cart = cartRepository.findByUserId(command.userId())
                .orElseThrow(() -> new CartItemNotFoundException("Cart not found for user"));

        cart.removeItem(command.productId());
        cartRepository.save(cart);

        return CartResponseMapper.toResponse(cart);
    }
}
