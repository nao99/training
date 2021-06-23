package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
class OrderItemTest {
    @Test
    public void changeCountIncrease() throws Exception {
        // given
        var orderItem = OrderItem.of("Alex", 10, BigDecimal.valueOf(1500L));

        // when
        orderItem.changeCount(15);

        // then
        assertEquals(15, orderItem.getCount());
        assertEquals(BigDecimal.valueOf(2250L), orderItem.getPrice());
    }

    @Test
    public void changeCountDecrease() throws Exception {
        // given
        var orderItem = OrderItem.of("Alex", 10, BigDecimal.valueOf(1500L));

        // when
        orderItem.changeCount(5);

        // then
        assertEquals(5, orderItem.getCount());
        assertEquals(BigDecimal.valueOf(750L), orderItem.getPrice());
    }

    @Test
    public void changeCountWhenCountTheSame() throws Exception {
        // given
        var orderItem = OrderItem.of("Alex", 10, BigDecimal.valueOf(1500L));

        var oldOrderItemCount = orderItem.getCount();
        var oldOrderItemPrice = orderItem.getPrice();

        // when
        orderItem.changeCount(10);

        // then
        assertEquals(oldOrderItemCount, orderItem.getCount());
        assertEquals(oldOrderItemPrice, orderItem.getPrice());
    }
}
