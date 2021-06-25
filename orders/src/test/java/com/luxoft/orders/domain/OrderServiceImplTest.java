package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.api.OrderItemRepository;
import com.luxoft.orders.persistent.api.OrderRepository;
import com.luxoft.orders.persistent.transaction.TransactionRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * OrderServiceImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-24
 */
class OrderServiceImplTest {
    private TransactionRunner transactionRunnerMock;
    private OrderRepository orderRepositoryMock;
    private OrderItemRepository orderItemRepositoryMock;

    private OrderServiceImpl service;

    @BeforeEach
    public void setUp() throws Exception {
        transactionRunnerMock = mock(TransactionRunner.class);
        orderRepositoryMock = mock(OrderRepository.class);
        orderItemRepositoryMock = mock(OrderItemRepository.class);

        service = new OrderServiceImpl(transactionRunnerMock, orderRepositoryMock, orderItemRepositoryMock);
    }

    @Test
    @Disabled
    public void getOrderWhenOrderExists() throws Exception {
        // given
        var orderId = 1L;
        var order = Order.builder()
            .username("Alex")
            .build();

        // when
        var orderResult = service.getOrder(orderId);

        // then
        assertEquals(order.getId(), orderResult.getId());
        assertEquals(order.getUsername(), orderResult.getUsername());
        assertEquals(order.isDone(), orderResult.isDone());
        assertEquals(order.getUpdatedAt(), orderResult.getUpdatedAt());
        assertEquals(order.getItems(), orderResult.getItems());
    }

    @Test
    @Disabled
    public void getOrderWhenOrderNotExists() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void createOrder() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void addOrderItemWhenOrderExists() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void addOrderItemWhenOrderNotExists() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void changeOrderItemCountWhenOrderItemExists() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void changeOrderItemCountWhenOrderItemNotExists() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void doneAllOrdersWhenSuchOrdersExists() throws Exception {
        // given
        // when
        // then
    }

    @Test
    @Disabled
    public void doneAllOrdersWhenSuchOrdersNotExists() throws Exception {
        // given
        // when
        // then
    }
}
