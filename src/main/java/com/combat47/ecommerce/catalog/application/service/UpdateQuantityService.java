package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.UpdateQuantityCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;
import com.combat47.ecommerce.catalog.application.port.in.UpdateQuantityUseCase;
import com.combat47.ecommerce.catalog.application.port.out.CartRepository;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import com.combat47.ecommerce.catalog.domain.model.Quantity;
import org.springframework.stereotype.Service;


@Service
public class UpdateQuantityService implements UpdateQuantityUseCase {

    private final CartRepository cartRepository;

    public UpdateQuantityService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartResponse updateQuantity(UpdateQuantityCommand command) {
        Cart cart = cartRepository.findByUserId(command.userId())
                .orElseThrow(() -> new CartItemNotFoundException("Cart not found for userId: " + command.userId()));

        Quantity newQuantity = new Quantity(command.quantity());
        cart.updateQuantity(command.productId(), newQuantity);
        cartRepository.save(cart);

        return CartResponseMapper.toResponse(cart);

    }
}
