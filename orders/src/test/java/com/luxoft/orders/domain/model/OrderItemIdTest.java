package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemIdTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderItemIdTest {
    /**
     * Test for {@link OrderItemId#of(long)}
     */
    @Test
    public void of() throws Exception {
        // given
        long id = 1L;

        // when
        OrderItemId orderId = OrderItemId.of(id);

        // then
        assertEquals(id, orderId.id());
    }

    /**
     * Test for {@link OrderItemId#of(long)}
     */
    @Test
    public void ofZero() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemId.of(0));
    }

    /**
     * Test for {@link OrderItemId#of(long)}
     */
    @Test
    public void ofNegative() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemId.of(-1));
    }
}
