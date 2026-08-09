package com.combat47.ecommerce.cart.infrastructure.web;


import com.combat47.ecommerce.cart.application.command.*;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.port.in.*;
import com.combat47.ecommerce.cart.infrastructure.web.request.AddToCartRequest;
import com.combat47.ecommerce.cart.infrastructure.web.request.UpdateCartItemQuantityRequest;
import com.combat47.ecommerce.identity.infrastructure.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final AddToCartUseCase addToCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final RemoveFromCartUseCase removeFromCartUseCase;
    private final ClearCartUseCase clearCartUseCase;

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        UUID customerId = principal.getId();
        AddToCartCommand command = new AddToCartCommand(customerId, request.productId(), request.quantity());
        CartResponse response = addToCartUseCase.addToCart(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal CustomUserPrincipal principal) {
        UUID customerId = principal.getId();
        GetCartCommand command = new GetCartCommand(customerId);
        CartResponse response = getCartUseCase.getCart(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateCartItemQuantityRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        UUID customerId = principal.getId();
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(customerId, productId, request.quantity());
        CartResponse response = updateCartItemQuantityUseCase.updateCartItemQuantity(command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable UUID productId,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        UUID customerId = principal.getId();
        RemoveFromCartCommand command = new RemoveFromCartCommand(customerId, productId);
        removeFromCartUseCase.removeFromCart(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(@AuthenticationPrincipal CustomUserPrincipal principal) {
        UUID customerId = principal.getId();
        ClearCartCommand command = new ClearCartCommand(customerId);
        clearCartUseCase.clearCart(command);
        return ResponseEntity.noContent().build();
    }
}
