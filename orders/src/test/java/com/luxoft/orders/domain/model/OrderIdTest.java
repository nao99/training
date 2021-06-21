package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderIdTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderIdTest {
    /**
     * Test for {@link OrderId#of(long)}
     */
    @Test
    public void of() throws Exception {
        // given
        long id = 1L;

        // when
        OrderId orderId = OrderId.of(id);

        // then
        assertEquals(id, orderId.id());
    }

    /**
     * Test for {@link OrderId#of(long)}
     */
    @Test
    public void ofZero() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderId.of(0));
    }

    /**
     * Test for {@link OrderId#of(long)}
     */
    @Test
    public void ofNegative() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderId.of(-1));
    }
}