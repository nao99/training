package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemNameTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderItemNameTest {
    /**
     * Test for {@link OrderItemName#of(String)}
     */
    @Test
    public void of() throws Exception {
        // given
        String name = "Alex";

        // when
        OrderItemName orderItemName = OrderItemName.of(name);

        // when
        assertEquals(name, orderItemName.name());
    }

    /**
     * Test for {@link OrderItemName#of(String)}
     */
    @Test
    public void ofTooSmallName() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemName.of("a"));
    }

    /**
     * Test for {@link OrderItemName#of(String)}
     */
    @Test
    public void ofTooLongName() throws Exception {
        // given
        String tooLongName = "a".repeat(201);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderItemName.of(tooLongName));
    }
}
