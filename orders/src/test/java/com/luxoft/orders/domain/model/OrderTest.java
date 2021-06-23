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
    /**
     * Test for {@link Order#done()}
     */
    @Test
    public void done() throws Exception {
        // given
        Order order = createOrder(1L, 1L);
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
        Long orderId = 1L;
        Long itemId = 1L;

        Order order = createOrder(orderId, itemId);
        int orderItemsSize = order.getItems().size();

        OrderItem item = OrderItem.of(2L, orderId, "Shoes", 15, BigDecimal.valueOf(1500L));

        // when
        order.addItem(item);

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
        OrderItem item = OrderItem.of(itemId, orderId, "Boots", 15, BigDecimal.valueOf(1500L));
        List<OrderItem> items = new ArrayList<>() {{
            add(item);
        }};

        return Order.of(orderId, "Alex", false, LocalDateTime.now(), items);
    }
}
