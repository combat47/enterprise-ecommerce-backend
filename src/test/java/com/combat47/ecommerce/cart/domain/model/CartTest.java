package com.combat47.ecommerce.cart.domain.model;

import com.combat47.ecommerce.cart.domain.exception.InvalidQuantityException;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.order.domain.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID_1 = UUID.randomUUID();
    private static final UUID PRODUCT_ID_2 = UUID.randomUUID();
    private static final String PRODUCT_NAME_1 = "Laptop";
    private static final String PRODUCT_NAME_2 = "Mouse";
    private static final Money PRICE_1 = new Money(new BigDecimal("999.99"));
    private static final Money PRICE_2 = new Money(new BigDecimal("29.99"));

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = Cart.create(CUSTOMER_ID);
    }

    @Test
    void should_create_empty_cart() {
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getCostumerId()).isEqualTo(CUSTOMER_ID);
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.totalQuantity()).isZero();
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(BigDecimal.ZERO));
        assertThat(cart.getCreatedAt()).isNotNull();
        assertThat(cart.getUpdatedAt()).isNotNull();
        assertThat(cart.getCreatedAt()).isEqualTo(cart.getUpdatedAt());
    }

    @Test
    void should_add_new_product() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.totalQuantity()).isEqualTo(2);
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(new BigDecimal("1999.98")));
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems().get(0).getProductName()).isEqualTo(PRODUCT_NAME_1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void should_increase_quantity_when_product_already_exists() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);

        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 3);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.totalQuantity()).isEqualTo(5);
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(new BigDecimal("4999.95")));
    }

    @Test
    void should_throw_when_adding_product_with_negative_quantity() {
        assertThatThrownBy(() -> cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, -1))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity must be positive");
    }

    @Test
    void should_throw_when_adding_product_with_zero_quantity() {
        assertThatThrownBy(() -> cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 0))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    void should_remove_product() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);
        cart.addProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRICE_2, 3);

        cart.removeProduct(PRODUCT_ID_1);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.totalQuantity()).isEqualTo(3);
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(new BigDecimal("89.97")));
    }

    @Test
    void should_throw_when_removing_non_existing_product() {
        assertThatThrownBy(() -> cart.removeProduct(PRODUCT_ID_1))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("Product not found in cart: " +  PRODUCT_ID_1);
    }

    @Test
    void should_change_product_quantity() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);

        cart.changeQuantity(PRODUCT_ID_1, 5);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.totalQuantity()).isEqualTo(5);
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(new BigDecimal("4999.95")));

    }

    @Test
    void should_remove_product_when_quantity_becomes_zero() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);

        cart.changeQuantity(PRODUCT_ID_1, 0);

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.totalQuantity()).isZero();
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(BigDecimal.ZERO));
        assertThat(cart.isEmpty()).isTrue();

    }

    @Test
    void should_throw_when_changing_quantity_to_negative() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);

        assertThatThrownBy(() -> cart.changeQuantity(PRODUCT_ID_1, -1))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity cannot be negative");
    }

    @Test
    void should_throw_when_changing_quantity_of_non_existing_product() {
        assertThatThrownBy(() -> cart.changeQuantity(PRODUCT_ID_1, 5))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("Product not found in cart: " +  PRODUCT_ID_1);
    }

    @Test
    void should_clear_cart() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);
        cart.addProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRICE_2, 3);

        cart.clear();

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.totalQuantity()).isZero();
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(BigDecimal.ZERO));
        assertThat(cart.isEmpty()).isTrue();

    }

    @Test
    void should_clear_empty_cart_without_exception() {
        cart.clear();

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.totalQuantity()).isZero();
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(BigDecimal.ZERO));
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void should_calculate_total_price() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2); //1999.98
        cart.addProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRICE_2, 3); //89.97

        Money expected = new Money(new BigDecimal("2089.95"));
        assertThat(cart.calculateTotalPrice()).isEqualTo(expected);
    }

    @Test
    void should_calculate_total_price_for_empty_cart() {
        assertThat(cart.calculateTotalPrice()).isEqualTo(new Money(BigDecimal.ZERO));
    }

    @Test
    void should_return_total_quantity() {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);
        cart.addProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRICE_2, 3);

        assertThat(cart.totalQuantity()).isEqualTo(5);

    }

    @Test
    void should_update_timestamp_on_add_product() throws InterruptedException {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);
        Thread.sleep(600);
        cart.addProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRICE_2, 1);
        assertThat(cart.getUpdatedAt()).isAfter(cart.getCreatedAt());
    }

    @Test
    void should_update_timestamp_on_remove_product() throws InterruptedException {
        cart.addProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRICE_1, 2);
        Thread.sleep(850);
        cart.removeProduct(PRODUCT_ID_1);
        assertThat(cart.getUpdatedAt()).isAfter(cart.getCreatedAt());
    }

    @Test
    void should_restore_cart() {
        UUID cartId = UUID.randomUUID();
        java.time.Instant now = java.time.Instant.now();

        Cart restored = Cart.restore(
                cartId,
                CUSTOMER_ID,
                null,
                now,
                now
        );

        assertThat(restored.getId()).isEqualTo(cartId);
        assertThat(restored.getCostumerId()).isEqualTo(CUSTOMER_ID);
        assertThat(restored.getItems()).isEmpty();
        assertThat(restored.getCreatedAt()).isEqualTo(now);
        assertThat(restored.getUpdatedAt()).isEqualTo(now);

    }

    @Test
    void cart_with_same_id_should_be_equal() {
        UUID id = UUID.randomUUID();
        java.time.Instant now = java.time.Instant.now();

        Cart c1 = Cart.restore(id, CUSTOMER_ID, null, now, now);
        Cart c2 = Cart.restore(id, CUSTOMER_ID, null, now, now);

        assertThat(c1).isEqualTo(c2);
        assertThat(c1.hashCode()).isEqualTo(c2.hashCode());

    }

    @Test
    void cart_with_different_id_should_not_be_equal() {
        java.time.Instant now = java.time.Instant.now();

        Cart c1 = Cart.restore(UUID.randomUUID(), CUSTOMER_ID, null, now, now);
        Cart c2 = Cart.restore(UUID.randomUUID(), CUSTOMER_ID, null, now, now);

        assertThat(c1).isNotEqualTo(c2);
    }
}




