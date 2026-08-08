package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.UpdateCartItemQuantityCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartItemResponse;
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
class UpdateCartItemQuantityServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartResponseMapper mapper;

    @InjectMocks
    private UpdateCartItemQuantityService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final int NEW_QUANTITY = 5;

    private final UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
            CUSTOMER_ID, PRODUCT_ID, NEW_QUANTITY
    );

    @Test
    void should_throw_when_cart_not_found() {

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCartItemQuantity(command))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage("Cart not found for customer: " + CUSTOMER_ID);

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void should_update_quantity_and_save() {

        Cart cart = Cart.create(CUSTOMER_ID);
        cart.addProduct(PRODUCT_ID, "Product", new Money(new BigDecimal("10.00")), 2);

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse expectedResponse = new CartResponse(
                cart.getId(),
                CUSTOMER_ID,
                List.of(new CartItemResponse(PRODUCT_ID, "Product", new BigDecimal("10.00"), NEW_QUANTITY, new BigDecimal("50.00"))),
                new BigDecimal("50.00"),
                NEW_QUANTITY
        );
        when(mapper.toResponse(any(Cart.class))).thenReturn(expectedResponse);

        CartResponse response = service.updateCartItemQuantity(command);

        assertThat(response).isNotNull();
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().getFirst().quantity()).isEqualTo(NEW_QUANTITY);

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository).save(cart);
        verify(mapper).toResponse(cart);
    }
}