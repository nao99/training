package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderUsernameTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderUsernameTest {
    /**
     * Test for {@link OrderUsername#of(String)}
     */
    @Test
    public void of() throws Exception {
        // given
        String name = "Alex";

        // when
        OrderUsername orderItemName = OrderUsername.of(name);

        // when
        assertEquals(name, orderItemName.username());
    }

    /**
     * Test for {@link OrderUsername#of(String)}
     */
    @Test
    public void ofTooSmallName() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderUsername.of("a"));
    }

    /**
     * Test for {@link OrderUsername#of(String)}
     */
    @Test
    public void ofTooLongName() throws Exception {
        // given
        String tooLongName = "a".repeat(201);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> OrderUsername.of(tooLongName));
    }
}
