package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.ClearCartCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.model.Cart;
import com.combat47.ecommerce.order.domain.model.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClearCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartResponseMapper mapper;

    @InjectMocks
    private ClearCartService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    private final ClearCartCommand command = new ClearCartCommand(CUSTOMER_ID);

    @Test
    void should_throw_when_cart_not_found() {

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.clearCart(command))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage("Cart not found for customer: " + CUSTOMER_ID);

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void should_clear_cart_and_save() {

        Cart cart = Cart.create(CUSTOMER_ID);
        cart.addProduct(UUID.randomUUID(), "Product", new Money(new BigDecimal("10.00")), 2);

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse expectedResponse = new CartResponse(
                cart.getId(),
                CUSTOMER_ID,
                List.of(),
                BigDecimal.ZERO,
                0
        );
        when(mapper.toResponse(any(Cart.class))).thenReturn(expectedResponse);

        CartResponse response = service.clearCart(command);

        assertThat(response).isNotNull();
        assertThat(response.items()).isEmpty();
        assertThat(response.quantity()).isZero();

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository).save(cart);
        verify(mapper).toResponse(cart);
    }
}