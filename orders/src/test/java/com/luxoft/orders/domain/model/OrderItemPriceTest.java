package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemPriceTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderItemPriceTest {
    /**
     * Test for {@link OrderItemPrice#of(BigDecimal)}
     */
    @Test
    public void of() throws Exception {
        // given
        BigDecimal price = BigDecimal.valueOf(155L);

        // when
        OrderItemPrice orderItemPrice = OrderItemPrice.of(price);

        // then
        assertEquals(price, orderItemPrice.price());
    }

    /**
     * Test for {@link OrderItemPrice#of(BigDecimal)}
     */
    @Test
    public void ofZeroPrice() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemPrice.of(BigDecimal.ZERO));
    }

    /**
     * Test for {@link OrderItemPrice#of(BigDecimal)}
     */
    @Test
    public void ofLessThanZeroPrice() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemPrice.of(BigDecimal.valueOf(-1L)));
    }

    /**
     * Test for {@link OrderItemPrice#divide(long)}
     */
    @Test
    public void divide() throws Exception {
        // given
        BigDecimal price = BigDecimal.valueOf(155L);
        OrderItemPrice orderItemPrice = OrderItemPrice.of(price);

        BigDecimal expectedPrice = BigDecimal.valueOf(51L);

        // when
        OrderItemPrice dividedOrderItemPrice = orderItemPrice.divide(3);

        // then
        assertEquals(expectedPrice, dividedOrderItemPrice.price());
    }

    /**
     * Test for {@link OrderItemPrice#multiply(long)}
     */
    @Test
    public void multiply() throws Exception {
        // given
        BigDecimal price = BigDecimal.valueOf(155L);
        OrderItemPrice orderItemPrice = OrderItemPrice.of(price);

        BigDecimal expectedPrice = BigDecimal.valueOf(310L);

        // when
        OrderItemPrice multipliedOrderItemPrice = orderItemPrice.multiply(2);

        // then
        assertEquals(expectedPrice, multipliedOrderItemPrice.price());
    }
}
