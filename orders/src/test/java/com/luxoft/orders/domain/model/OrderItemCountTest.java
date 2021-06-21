package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemCountTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderItemCountTest {
    /**
     * Test for {@link OrderItemCount#of(int)}
     */
    @Test
    public void of() throws Exception {
        // given
        int count = 1;

        // when
        OrderItemCount orderCount = OrderItemCount.of(count);

        // then
        assertEquals(count, orderCount.count());
    }

    /**
     * Test for {@link OrderItemCount#of(int)}
     */
    @Test
    public void ofZero() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemCount.of(0));
    }

    /**
     * Test for {@link OrderItemCount#of(int)}
     */
    @Test
    public void ofNegative() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemCount.of(-1));
    }
}
