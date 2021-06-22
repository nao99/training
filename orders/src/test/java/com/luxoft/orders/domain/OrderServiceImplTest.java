package com.luxoft.orders.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private OrderRepository orderRepositoryMock;

    private OrderServiceImpl service;

    @BeforeEach
    public void setUp() throws Exception {
        orderRepositoryMock = mock(OrderRepository.class);
        service = new OrderServiceImpl(orderRepositoryMock);
    }

    @Test
    public void getOrder() throws Exception {
        // given
        // when
        // then
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
