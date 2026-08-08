package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.AddToCartCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.model.ProductSnapshot;
import com.combat47.ecommerce.cart.application.port.in.AddToCartUseCase;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.application.port.out.ProductCatalogPort;
import com.combat47.ecommerce.cart.domain.model.Cart;
import com.combat47.ecommerce.cart.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.order.domain.model.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartAddToCartService implements AddToCartUseCase {

    private final CartRepository cartRepository;
    private final ProductCatalogPort productCatalogPort;
    private final CartResponseMapper mapper;

    public CartAddToCartService(CartRepository cartRepository, ProductCatalogPort productCatalogPort, CartResponseMapper cartResponseMapper) {
        this.cartRepository = cartRepository;
        this.productCatalogPort = productCatalogPort;
        this.mapper = cartResponseMapper;
    }

    @Override
    @Transactional
    public CartResponse addToCart(AddToCartCommand command) {
        ProductSnapshot product = productCatalogPort.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + command.productId()));

        if (!product.active()) {
            throw new IllegalArgumentException("Product is  not active: " + command.productId());
        }

        Cart cart = cartRepository.findByCustomerId(command.customerId())
                .orElseGet(() -> Cart.create(command.customerId()));

        cart.addProduct(
                product.productId(),
                product.name(),
                new Money(product.price()),
                command.quantity()
        );

        Cart savedCart = cartRepository.save(cart);

        return mapper.toResponse(savedCart);
    }
}
