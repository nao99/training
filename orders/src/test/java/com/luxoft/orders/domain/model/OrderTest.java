package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderTest {
    /**
     * Test for {@link Order#withId(OrderId)}
     */
    @Test
    public void withId() throws Exception {
        // given
        OrderId id = OrderId.of(1L);
        Order order = Order.of(OrderUsername.of("Alex"));

        // when
        Order result = order.withId(id);

        // then
        assertEquals(id, result.getId());

        assertEquals(order.getUsername(), result.getUsername());
        assertEquals(order.isDone(), result.isDone());
        assertEquals(order.getItems(), result.getItems());
        assertEquals(order.getUpdatedAt(), result.getUpdatedAt());
    }

    /**
     * Test for {@link Order#done()}
     */
    @Test
    public void done() throws Exception {
        // given
        Order order = Order.of(OrderUsername.of("Alex"));
        boolean orderIsDone = order.isDone();

        // when
        order.done();

        // then
        assertFalse(orderIsDone);
        assertTrue(order.isDone());
    }

    /**
     * Test for {@link Order#addItem(OrderItem)}
     */
    @Test
    public void addItem() throws Exception {
        // given
        Order order = Order.of(OrderId.of(1L), OrderUsername.of("Alex"), false, Timestamp.from(Instant.now()));
        int orderItemsSize = order.getItems().size();

        OrderItem orderItem = OrderItem.of(
            OrderItemId.of(1L),
            order.getId(),
            OrderItemName.of("Мокасины"),
            OrderItemCount.of(4),
            OrderItemPrice.of(BigDecimal.valueOf(40))
        );

        // when
        order.addItem(orderItem);

        // then
        assertNotEquals(orderItemsSize, order.getItems().size());
        assertEquals(orderItem, order.getItems().iterator().next());
    }
}
