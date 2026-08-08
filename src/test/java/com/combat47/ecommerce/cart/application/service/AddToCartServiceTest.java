package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.AddToCartCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartItemResponse;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.model.ProductSnapshot;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.application.port.out.ProductCatalogPort;
import com.combat47.ecommerce.cart.domain.model.Cart;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddToCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartResponseMapper mapper;

    @Mock
    private ProductCatalogPort productCatalogPort;

    @InjectMocks
    private CartAddToCartService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final String PRODUCT_NAME = "Laptop";
    private static final BigDecimal PRICE = new BigDecimal("999.99");
    private static final int QUANTITY = 2;

    private final AddToCartCommand command =
            new AddToCartCommand(
                    CUSTOMER_ID,
                    PRODUCT_ID,
                    QUANTITY
            );

    @Test
    void should_create_new_cart_when_non_exists() {

        ProductSnapshot product =
                new ProductSnapshot(
                        PRODUCT_ID,
                        PRODUCT_NAME,
                        PRICE,
                        true
                );

        when(productCatalogPort.findById(PRODUCT_ID))
                .thenReturn(Optional.of(product));

        when(cartRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.empty());

        Cart newCart = Cart.create(CUSTOMER_ID);

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(newCart);

        CartResponse expectedResponse =
                new CartResponse(
                        newCart.getId(),
                        CUSTOMER_ID,
                        List.of(),
                        BigDecimal.ZERO,
                        0
                );

        when(mapper.toResponse(any(Cart.class)))
                .thenReturn(expectedResponse);

        CartResponse response =
                service.addToCart(command);

        assertThat(response).isNotNull();
        assertThat(response.customerId())
                .isEqualTo(CUSTOMER_ID);

        verify(productCatalogPort)
                .findById(PRODUCT_ID);

        verify(cartRepository)
                .findByCustomerId(CUSTOMER_ID);

        verify(cartRepository)
                .save(any(Cart.class));

        verify(mapper)
                .toResponse(any(Cart.class));
    }

    @Test
    void should_use_existing_cart_when_found() {

        ProductSnapshot product =
                new ProductSnapshot(
                        PRODUCT_ID,
                        PRODUCT_NAME,
                        PRICE,
                        true
                );

        when(productCatalogPort.findById(PRODUCT_ID))
                .thenReturn(Optional.of(product));

        Cart existingCart =
                Cart.create(CUSTOMER_ID);

        when(cartRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(existingCart));

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(existingCart);

        CartResponse expectedResponse =
                new CartResponse(
                        existingCart.getId(),
                        CUSTOMER_ID,
                        List.of(),
                        BigDecimal.ZERO,
                        0
                );

        when(mapper.toResponse(any(Cart.class)))
                .thenReturn(expectedResponse);

        CartResponse response =
                service.addToCart(command);

        assertThat(response).isNotNull();
        assertThat(response.customerId())
                .isEqualTo(CUSTOMER_ID);

        verify(productCatalogPort)
                .findById(PRODUCT_ID);

        verify(cartRepository)
                .findByCustomerId(CUSTOMER_ID);

        verify(cartRepository)
                .save(any(Cart.class));

        verify(mapper)
                .toResponse(any(Cart.class));
    }

    @Test
    void should_add_product_to_cart_and_save() {

        ProductSnapshot product =
                new ProductSnapshot(
                        PRODUCT_ID,
                        PRODUCT_NAME,
                        PRICE,
                        true
                );

        when(productCatalogPort.findById(PRODUCT_ID))
                .thenReturn(Optional.of(product));

        Cart cart =
                Cart.create(CUSTOMER_ID);

        when(cartRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(cart));

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        CartResponse expectedResponse =
                new CartResponse(
                        cart.getId(),
                        CUSTOMER_ID,
                        List.of(
                                new CartItemResponse(
                                        PRODUCT_ID,
                                        PRODUCT_NAME,
                                        PRICE,
                                        QUANTITY,
                                        PRICE.multiply(
                                                BigDecimal.valueOf(QUANTITY)
                                        )
                                )
                        ),
                        PRICE.multiply(
                                BigDecimal.valueOf(QUANTITY)
                        ),
                        QUANTITY
                );

        when(mapper.toResponse(any(Cart.class)))
                .thenReturn(expectedResponse);

        CartResponse response =
                service.addToCart(command);

        assertThat(response).isNotNull();
        assertThat(response.items())
                .hasSize(1);

        assertThat(response.items().getFirst().productId())
                .isEqualTo(PRODUCT_ID);

        assertThat(response.items().getFirst().quantity())
                .isEqualTo(QUANTITY);

        assertThat(response.items().getFirst().unitPrice())
                .isEqualTo(PRICE);

        assertThat(response.items().getFirst().subtotal())
                .isEqualTo(
                        PRICE.multiply(
                                BigDecimal.valueOf(QUANTITY)
                        )
                );

        assertThat(response.totalPrice())
                .isEqualTo(
                        PRICE.multiply(
                                BigDecimal.valueOf(QUANTITY)
                        )
                );

        assertThat(response.quantity())
                .isEqualTo(QUANTITY);

        verify(productCatalogPort)
                .findById(PRODUCT_ID);

        verify(cartRepository)
                .findByCustomerId(CUSTOMER_ID);

        verify(cartRepository)
                .save(any(Cart.class));

        verify(mapper)
                .toResponse(any(Cart.class));
    }
}