package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.UpdateCartItemQuantityCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.port.in.UpdateCartItemQuantityUseCase;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UpdateCartItemQuantityService implements UpdateCartItemQuantityUseCase {

    private final CartRepository cartRepository;
    private final CartResponseMapper mapper;

    public UpdateCartItemQuantityService(CartRepository cartRepository, CartResponseMapper mapper) {
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CartResponse updateCartItemQuantity(UpdateCartItemQuantityCommand command) {
        Cart cart = cartRepository.findByCustomerId(command.customerId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for customer: " + command.customerId()));

        cart.changeQuantity(command.productId(), command.quantity());

        Cart savedCart = cartRepository.save(cart);

        return mapper.toResponse(savedCart);
    }
}
