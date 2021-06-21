package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderItemTest {
    /**
     * Test for {@link OrderItem#replaceCount(int)}
     */
    @Test
    public void replaceCountByIncreasing() throws Exception {
        // given
        OrderItem item = OrderItem.of(
            OrderItemId.of(1L),
            OrderId.of(1L),
            OrderItemName.of("Мокасины"),
            OrderItemCount.of(4),
            OrderItemPrice.of(BigDecimal.valueOf(40))
        );

        // when
        item.replaceCount(5);

        // then
        assertEquals(5, item.getCount().count());
        assertEquals(BigDecimal.valueOf(50), item.getPrice().price());
    }

    /**
     * Test for {@link OrderItem#replaceCount(int)}
     */
    @Test
    public void replaceCountByDecreasing() throws Exception {
        // given
        OrderItem item = OrderItem.of(
            OrderItemId.of(1L),
            OrderId.of(1L),
            OrderItemName.of("Мокасины"),
            OrderItemCount.of(4),
            OrderItemPrice.of(BigDecimal.valueOf(40))
        );

        // when
        item.replaceCount(2);

        // then
        assertEquals(2, item.getCount().count());
        assertEquals(BigDecimal.valueOf(20), item.getPrice().price());
    }

    /**
     * Test for {@link OrderItem#replaceCount(int)}
     */
    @Test
    public void replaceCountWhenCountTheSame() throws Exception {
        // given
        OrderItemCount itemCount = OrderItemCount.of(4);
        OrderItemPrice itemPrice = OrderItemPrice.of(BigDecimal.valueOf(40));

        OrderItem item = OrderItem.of(
            OrderItemId.of(1L),
            OrderId.of(1L),
            OrderItemName.of("Мокасины"),
            itemCount,
            itemPrice
        );

        // when
        item.replaceCount(4);

        // then
        assertSame(itemCount, item.getCount());
        assertSame(itemPrice, item.getPrice());
    }
}
