package com.combat47.ecommerce.cart.application.mapper;

import com.combat47.ecommerce.cart.application.model.CartItemResponse;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.domain.model.Cart;
import com.combat47.ecommerce.cart.domain.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CartResponseMapper {

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                items,
                cart.calculateTotalPrice().getAmount(),
                cart.totalQuantity()
        );
    }

    private CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice().getAmount(),
                item.getQuantity(),
                item.subtotal().getAmount()
        );
    }

}
