package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.RemoveFromCartCommand;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveFromCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartResponseMapper mapper;

    @InjectMocks
    private CartRemoveFromCartService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    private final RemoveFromCartCommand command = new RemoveFromCartCommand(CUSTOMER_ID, PRODUCT_ID);

    @Test
    void should_throw_when_cart_not_found() {

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeFromCart(command))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage("Cart not found for customer: " + CUSTOMER_ID);

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());

    }

    @Test
    void should_remove_product_and_save() {

        Cart cart = Cart.create(CUSTOMER_ID);
        cart.addProduct(PRODUCT_ID, "Product", new Money(new BigDecimal("10.00")), 2);

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse expectedResponse = new CartResponse(
                cart.getId(),
                CUSTOMER_ID,
                List.of(),
                BigDecimal.ZERO,
                0
        );
        when(mapper.toResponse(any())).thenReturn(expectedResponse);

        CartResponse response = service.removeFromCart(command);

        assertThat(response).isNotNull();
        assertThat(response.items()).isEmpty();
        assertThat(response.quantity()).isZero();

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository).save(cart);
        verify(mapper).toResponse(cart);

    }

}
