package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.persistent.transaction.TransactionRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * OrderServiceImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
class OrderServiceImplTest {
    private TransactionRunner transactionRunnerMock;
    private OrderRepository orderRepositoryMock;
    private OrderServiceImpl service;

    @BeforeEach
    public void setUp() throws Exception {
        transactionRunnerMock = mock(TransactionRunner.class);
        orderRepositoryMock = mock(OrderRepository.class);
        service = new OrderServiceImpl(transactionRunnerMock, orderRepositoryMock);
    }

    @Test
    @Disabled
    public void getOrder() throws Exception {
        // given
        var orderId = 1L;

        var expectedOrder = Order.of("Alex");
        expectedOrder = expectedOrder.withId(orderId);

        var connectionMock = mock(Connection.class);

        // when
        when(orderRepositoryMock.findById(connectionMock, orderId))
            .thenReturn(Optional.of(expectedOrder));

        var foundOrder = service.getOrder(orderId);

        verify(orderRepositoryMock, times(1))
            .findById(connectionMock, orderId);

        // then
        assertSame(expectedOrder, foundOrder);
    }

    @Test
    @Disabled
    public void getOrderWhenOrderNotFound() throws Exception {
        // given
        var orderId = 1L;
        var connectionMock = mock(Connection.class);

        // when
        when(orderRepositoryMock.findById(connectionMock, orderId))
            .thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.getOrder(orderId));

        verify(orderRepositoryMock, times(1))
            .findById(connectionMock, orderId);
    }

    @Test
    public void create() throws Exception {
        // given
        // when
        // then
    }

    @Test
    public void addOrderItem() throws Exception {
        // given
        // when
        // then
    }

    @Test
    public void changeOrderItemCount() throws Exception {
        // given
        // when
        // then
    }

    @Test
    public void doneAllOrders() throws Exception {
        // given
        // when
        // then
    }
}
