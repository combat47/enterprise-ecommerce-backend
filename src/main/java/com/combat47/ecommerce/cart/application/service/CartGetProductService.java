package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.GetCartCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.port.in.GetCartUseCase;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartGetProductService implements GetCartUseCase {

    private final CartRepository cartRepository;
    private final CartResponseMapper mapper;

    public CartGetProductService(CartRepository cartRepository, CartResponseMapper mapper) {
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(GetCartCommand command) {
        Cart cart = cartRepository.findByCustomerId(command.customerId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for customer: " + command.customerId()));

        return mapper.toResponse(cart);
    }
}
