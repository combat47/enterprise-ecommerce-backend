package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.AddToCartCommand;
import com.combat47.ecommerce.catalog.application.model.CartResponse;
import com.combat47.ecommerce.catalog.application.port.out.CartRepository;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import com.combat47.ecommerce.catalog.domain.model.CartItem;
import com.combat47.ecommerce.catalog.domain.model.Price;
import com.combat47.ecommerce.catalog.domain.model.Quantity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductToCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CatalogAddToCartService addToCartService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    private final AddToCartCommand command = new AddToCartCommand(
            USER_ID, PRODUCT_ID, "Test Product", new BigDecimal("99.99"), 2
    );

    @Test
    void should_add_to_existing_cart() {
        Cart cart = Cart.create(USER_ID);
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        CartResponse response = addToCartService.addToCart(command);

        assertNotNull(response);
        assertEquals(1, response.itemCount());
        assertEquals(new BigDecimal("199.98"), response.total());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void should_create_new_cart_if_not_exists() {
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        CartResponse response = addToCartService.addToCart(command);

        assertNotNull(response);
        assertEquals(1, response.itemCount());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void should_increase_quantity_if_product_exists() {
        Cart cart = Cart.create(USER_ID);
        CartItem existing = CartItem.create(PRODUCT_ID, "Test Product", new Price(new BigDecimal("99.99")), new Quantity(1));
        cart.addItem(existing);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        CartResponse response = addToCartService.addToCart(command);

        assertEquals(1, response.itemCount());
        assertEquals(2, response.items().getFirst().quantity());
        assertEquals(new BigDecimal("199.98"), response.total());
    }
}