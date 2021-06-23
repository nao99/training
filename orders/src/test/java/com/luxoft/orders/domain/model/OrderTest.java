package com.luxoft.orders.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class OrderTest {
    @Test
    public void done() throws Exception {
        // given
        var order = createOrder(1L, 1L);
        var orderIsDone = order.isDone();

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
        var orderId = 1L;
        var itemId = 1L;

        var order = createOrder(orderId, itemId);
        var orderItemsSize = order.getItems().size();

        var orderItem = OrderItem.of(2L, orderId, "Shoes", 15, BigDecimal.valueOf(1500L));

        // when
        order.addItem(orderItem);

        // then
        assertEquals(1, order.getItems().size() - orderItemsSize);
    }

    /**
     * Creates a new {@link Order} for testing purposes
     *
     * @param orderId an order id
     * @param itemId  an order item id
     *
     * @return a new order
     */
    private Order createOrder(Long orderId, Long itemId) {
        var orderItem = OrderItem.of(itemId, orderId, "Boots", 15, BigDecimal.valueOf(1500L));
        List<OrderItem> items = new ArrayList<>() {{
            add(orderItem);
        }};

        return Order.of(orderId, "Alex", false, LocalDateTime.now(), items);
    }
}
