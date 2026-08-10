package com.combat47.ecommerce.cart.infrastructure.web;

import com.combat47.ecommerce.cart.application.command.CheckoutCommand;
import com.combat47.ecommerce.cart.application.port.in.CheckoutUseCase;
import com.combat47.ecommerce.identity.infrastructure.security.CustomUserPrincipal;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CheckoutController {

    private final CheckoutUseCase checkoutUseCase;

    public CheckoutController(CheckoutUseCase checkoutUseCase) {
        this.checkoutUseCase = checkoutUseCase;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        CheckoutCommand command = new CheckoutCommand(principal.getId());
        OrderResponse response = checkoutUseCase.checkout(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}