package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.CheckoutCommand;
import com.combat47.ecommerce.cart.application.port.in.CheckoutUseCase;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.application.port.out.ProductCatalogPort;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.cart.domain.model.Cart;
import com.combat47.ecommerce.cart.domain.model.CartItem;
import com.combat47.ecommerce.order.application.command.PlaceOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.PlaceOrderUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CheckoutService implements CheckoutUseCase {

    private final CartRepository cartRepository;
    private final ProductCatalogPort productCatalogPort;
    private final PlaceOrderUseCase placeOrderUseCase;

    public CheckoutService(CartRepository cartRepository, ProductCatalogPort productCatalogPort, PlaceOrderUseCase placeOrderUseCase) {
        this.cartRepository = cartRepository;
        this.productCatalogPort = productCatalogPort;
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutCommand command) {
        Cart cart = cartRepository.findByCustomerId(command.customerId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for customer: " + command.customerId()));

        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        List<PlaceOrderCommand.OrderItemCommand> orderItems = cart.getItems().stream()
                .map(this::toOrderItemCommand)
                .toList();

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(command.customerId(), orderItems);
        OrderResponse orderResponse = placeOrderUseCase.placeOrder(placeOrderCommand);

        cart.clear();
        cartRepository.save(cart);

        return orderResponse;
    }

    private PlaceOrderCommand.OrderItemCommand toOrderItemCommand(CartItem cartItem) {
        var productSnapshot = productCatalogPort.findById(cartItem.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found in catalog: " + cartItem.getProductId()
                ));

        return new PlaceOrderCommand.OrderItemCommand(
                cartItem.getProductId(),
                productSnapshot.name(),
                productSnapshot.price(),
                cartItem.getQuantity()
        );
    }
}
