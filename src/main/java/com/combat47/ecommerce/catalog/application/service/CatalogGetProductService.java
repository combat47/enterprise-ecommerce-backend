package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.GetCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;
import com.combat47.ecommerce.catalog.application.port.in.GetCartUseCase;
import com.combat47.ecommerce.catalog.application.port.out.CartRepository;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import org.springframework.stereotype.Service;


@Service
public class CatalogGetProductService implements GetCartUseCase {

    private final CartRepository cartRepository;


    public CatalogGetProductService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartResponse getCart(GetCartCommand command) {
        Cart cart = cartRepository.findByUserId(command.userId())
                .orElseThrow(() -> new CartItemNotFoundException("Cart not found for user"));

        return CartResponseMapper.toResponse(cart);
    }
}
