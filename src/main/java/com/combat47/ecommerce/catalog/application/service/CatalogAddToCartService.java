package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.AddToCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;
import com.combat47.ecommerce.catalog.application.port.in.AddToCartUseCase;
import com.combat47.ecommerce.catalog.application.port.out.CartRepository;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import com.combat47.ecommerce.catalog.domain.model.CartItem;
import com.combat47.ecommerce.catalog.domain.model.Price;
import com.combat47.ecommerce.catalog.domain.model.Quantity;
import org.springframework.stereotype.Service;


@Service
public class CatalogAddToCartService implements AddToCartUseCase {


    private final CartRepository cartRepository;

    public CatalogAddToCartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartResponse addToCart(AddToCartCommand command) {
        Cart cart = cartRepository.findByUserId(command.userId())
                .orElseGet(() -> Cart.create(command.userId()));

        Price price = new Price(command.price());
        Quantity quantity = new Quantity(command.quantity());
        CartItem item = CartItem.create(
                command.productId(),
                command.productName(),
                price,
                quantity
        );
        cart.addItem(item);
        cartRepository.save(cart);

        return CartResponseMapper.toResponse(cart);
    }
}
