package com.combat47.ecommerce.cart.domain.model;

import com.combat47.ecommerce.cart.domain.exception.InvalidQuantityException;
import com.combat47.ecommerce.order.domain.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    public static final UUID PRODUCT_ID = UUID.randomUUID();
    public static final String PRODUCT_NAME = "Laptop";
    public static final Money PRICE = new Money(new BigDecimal("999.99"));

    @Test
    void should_create_cart_item() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        assertThat(item.getId()).isNotNull();
        assertThat(item.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(item.getProductName()).isEqualTo(PRODUCT_NAME);
        assertThat(item.getUnitPrice()).isEqualTo(PRICE);
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.subtotal()).isEqualTo(new Money(new BigDecimal("1999.98")));
    }

    @Test
    void should_restore_cart_item() {
        UUID id = UUID.randomUUID();
        CartItem item = CartItem.restore(id, PRODUCT_ID, PRODUCT_NAME, PRICE, 3);

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(item.getProductName()).isEqualTo(PRODUCT_NAME);
        assertThat(item.getQuantity()).isEqualTo(3);
        assertThat(item.subtotal()).isEqualTo(new Money(new BigDecimal("2999.97")));
    }

    @Test
    void should_increase_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        item.increase();

        assertThat(item.getQuantity()).isEqualTo(3);
    }

    @Test
    void should_increase_by_amount() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);
        item.increase(3);
        assertThat(item.getQuantity()).isEqualTo(5);
    }

    @Test
    void should_throw_when_increase_by_negative_or_zero() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        assertThatThrownBy(() -> item.increase(0))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Increase amount must be positive");

        assertThatThrownBy(() -> item.increase(-1))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Increase amount must be positive");
    }

    @Test
    void should_decrease_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        item.decrease();

        assertThat(item.getQuantity()).isEqualTo(1);
    }

    @Test
    void should_throw_decrease_below_one() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 1);

        assertThatThrownBy(item::decrease)
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity cannot be less than 1");
    }

    @Test
    void should_change_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        item.changeQuantity(5);

        assertThat(item.getQuantity()).isEqualTo(5);
    }

    @Test
    void should_throw_when_changing_to_negative_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        assertThatThrownBy(() ->  item.changeQuantity(-1))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity cannot be negative");
    }

    @Test
    void should_calculate_subtotal() {
        CartItem item = CartItem.create(PRODUCT_ID, PRODUCT_NAME, PRICE, 3);

        assertThat(item.subtotal()).isEqualTo(new Money(new BigDecimal("2999.97")));
    }

    @Test
    void items_with_same_id_should_be_equal() {
        UUID id = UUID.randomUUID();
        CartItem item1 = CartItem.restore(id, PRODUCT_ID, PRODUCT_NAME, PRICE, 2);
        CartItem item2 = CartItem.restore(id, PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    void items_with_different_id_should_not_be_equal() {
        CartItem item1 = CartItem.restore(UUID.randomUUID(), PRODUCT_ID, PRODUCT_NAME, PRICE, 2);
        CartItem item2 = CartItem.restore(UUID.randomUUID(), PRODUCT_ID, PRODUCT_NAME, PRICE, 2);

        assertThat(item1).isNotEqualTo(item2);
    }
}
