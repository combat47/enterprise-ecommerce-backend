package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.CheckoutCommand;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.application.port.out.ProductCatalogPort;
import com.combat47.ecommerce.cart.application.model.ProductSnapshot;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.cart.domain.model.Cart;
import com.combat47.ecommerce.order.application.command.PlaceOrderCommand;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.combat47.ecommerce.order.application.port.in.PlaceOrderUseCase;
import com.combat47.ecommerce.order.domain.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductCatalogPort productCatalogPort;

    @Mock
    private PlaceOrderUseCase placeOrderUseCase;

    @InjectMocks
    private CheckoutService checkoutService;

    private UUID customerId;
    private UUID productId;
    private Cart cart;
    private ProductSnapshot productSnapshot;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        productId = UUID.randomUUID();
        cart = Cart.create(customerId);
        cart.addProduct(productId, "Old Name", new Money(new BigDecimal("100")), 2);
        productSnapshot = new ProductSnapshot(productId, "Real Product", new BigDecimal("120"), true);
    }

    @Test
    void shouldCheckoutSuccessfully() {
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(productCatalogPort.findById(productId)).thenReturn(Optional.of(productSnapshot));

        OrderResponse expectedResponse = mock(OrderResponse.class);
        when(placeOrderUseCase.placeOrder(any(PlaceOrderCommand.class))).thenReturn(expectedResponse);

        CheckoutCommand command = new CheckoutCommand(customerId);
        OrderResponse response = checkoutService.checkout(command);

        assertThat(response).isEqualTo(expectedResponse);
        assertThat(cart.isEmpty()).isTrue();
        verify(cartRepository).save(cart);
    }

    @Test
    void shouldThrowWhenCartNotFound() {
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        CheckoutCommand command = new CheckoutCommand(customerId);
        assertThatThrownBy(() -> checkoutService.checkout(command))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessageContaining("Cart not found");
    }

    @Test
    void shouldThrowWhenCartIsEmpty() {
        cart.clear();
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        CheckoutCommand command = new CheckoutCommand(customerId);
        assertThatThrownBy(() -> checkoutService.checkout(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot checkout an empty cart");
    }

    @Test
    void shouldThrowWhenProductNotFoundInCatalog() {
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(productCatalogPort.findById(productId)).thenReturn(Optional.empty());

        CheckoutCommand command = new CheckoutCommand(customerId);
        assertThatThrownBy(() -> checkoutService.checkout(command))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found in catalog");
    }
}