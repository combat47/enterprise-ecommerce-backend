package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.model.CartResponse;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import com.combat47.ecommerce.catalog.domain.model.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponseMapper {

    private  CartResponseMapper() {
    }

    static CartResponse toResponse(Cart cart) {
        List<CartResponse.CartItemResponse> items = cart.getItems().stream()
                .map(CartResponseMapper::toItemResponse)
                .collect(Collectors.toList());

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                items,
                cart.calculateTotal(),
                cart.itemCount()
        );
    }

    private static CartResponse.CartItemResponse toItemResponse(CartItem item) {
        return new CartResponse.CartItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getPrice().getValue(),
                item.getQuantity().getValue(),
                item.totalPrice()
        );
    }
}
