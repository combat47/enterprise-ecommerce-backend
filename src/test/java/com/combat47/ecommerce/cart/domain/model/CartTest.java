package com.combat47.ecommerce.cart.domain.model;

import com.combat47.ecommerce.cart.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.cart.domain.exception.InvalidQuantityException;
import com.combat47.ecommerce.order.domain.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    private UUID customerId;
    private UUID productId1;
    private UUID productId2;
    private String productName1;
    private String productName2;
    private Money unitPrice1;
    private Money unitPrice2;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        productName1 = "Laptop";
        productName2 = "Mouse";
        unitPrice1 = new Money(new BigDecimal("999.99"));
        unitPrice2 = new Money(new BigDecimal("19.99"));
        cart = Cart.create(customerId);
    }

    @Test
    void shouldCreateNewCart() {
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getCustomerId()).isEqualTo(customerId);
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.itemCount()).isZero();
        assertThat(cart.totalQuantity()).isZero();
        assertThat(cart.calculateTotalPrice().getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldAddProductToEmptyCart() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);

        assertThat(cart.getItems()).hasSize(1);
        CartItem item = cart.getItems().getFirst();
        assertThat(item.getProductId()).isEqualTo(productId1);
        assertThat(item.getProductName()).isEqualTo(productName1);
        assertThat(item.getUnitPrice()).isEqualTo(unitPrice1);
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.subtotal().getAmount()).isEqualByComparingTo(new BigDecimal("1999.98"));
        assertThat(cart.totalQuantity()).isEqualTo(2);
        assertThat(cart.calculateTotalPrice().getAmount()).isEqualByComparingTo(new BigDecimal("1999.98"));
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.itemCount()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseQuantityWhenAddingExistingProduct() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);
        cart.addProduct(productId1, productName1, unitPrice1, 3);

        CartItem item = cart.getItems().getFirst();
        assertThat(item.getQuantity()).isEqualTo(5);
        assertThat(cart.totalQuantity()).isEqualTo(5);
        assertThat(cart.itemCount()).isEqualTo(1);
    }

    @Test
    void shouldAddDifferentProducts() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);
        cart.addProduct(productId2, productName2, unitPrice2, 3);

        assertThat(cart.getItems()).hasSize(2);
        assertThat(cart.totalQuantity()).isEqualTo(5);
        assertThat(cart.calculateTotalPrice().getAmount())
                .isEqualByComparingTo(new BigDecimal("2059.95"));
    }

    @Test
    void shouldThrowWhenAddingProductWithZeroQuantity() {
        assertThatThrownBy(() -> cart.addProduct(productId1, productName1, unitPrice1, 0))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    void shouldThrowWhenAddingProductWithNegativeQuantity() {
        assertThatThrownBy(() -> cart.addProduct(productId1, productName1, unitPrice1, -1))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    void shouldRemoveExistingProduct() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);
        cart.removeProduct(productId1);

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.totalQuantity()).isZero();
    }

    @Test
    void shouldThrowWhenRemovingNonExistingProduct() {
        assertThatThrownBy(() -> cart.removeProduct(productId1))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessageContaining("Product not found in cart");
    }

    @Test
    void shouldChangeQuantityOfExistingProduct() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);
        cart.changeQuantity(productId1, 5);

        CartItem item = cart.getItems().getFirst();
        assertThat(item.getQuantity()).isEqualTo(5);
        assertThat(cart.totalQuantity()).isEqualTo(5);
    }

    @Test
    void shouldRemoveProductWhenChangingQuantityToZero() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);
        cart.changeQuantity(productId1, 0);

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void shouldThrowWhenChangingQuantityToNegative() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);

        assertThatThrownBy(() -> cart.changeQuantity(productId1, -1))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("Quantity cannot be negative");
    }

    @Test
    void shouldThrowWhenChangingQuantityOfNonExistingProduct() {
        assertThatThrownBy(() -> cart.changeQuantity(productId1, 3))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessageContaining("Product not found in cart");
    }

    @Test
    void shouldClearCartWithItems() {
        cart.addProduct(productId1, productName1, unitPrice1, 2);
        cart.addProduct(productId2, productName2, unitPrice2, 3);
        cart.clear();

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.totalQuantity()).isZero();
    }

    @Test
    void shouldClearEmptyCartWithoutError() {
        cart.clear();
        assertThat(cart.getItems()).isEmpty();
    }
}