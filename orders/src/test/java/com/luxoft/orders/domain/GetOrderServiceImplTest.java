package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.OrderId;
import com.luxoft.orders.infrastructure.transaction.TransactionRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GetOrderServiceImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class GetOrderServiceImplTest {
    /**
     * Transaction runner
     */
    private TransactionRunner transactionRunnerMock;

    /**
     * Order repository
     */
    private OrderRepository orderRepositoryMock;

    /**
     * Order item repository
     */
    private OrderItemRepository orderItemRepositoryMock;

    /**
     * Get order service
     */
    private GetOrderServiceImpl getOrderService;

    /**
     * Sets up
     */
    @BeforeEach
    public void setUp() {
        transactionRunnerMock = mock(TransactionRunner.class);
        orderRepositoryMock = mock(OrderRepository.class);
        orderItemRepositoryMock = mock(OrderItemRepository.class);
        getOrderService = new GetOrderServiceImpl(transactionRunnerMock, orderRepositoryMock, orderItemRepositoryMock);
    }

    /**
     * Test for {@link GetOrderServiceImpl#getOrder(OrderId)}
     */
    @Test
    @Disabled
    public void getOrder() throws Exception {
        // given
        // when
        // then
    }

    /**
     * Test for {@link GetOrderServiceImpl#getOrder(OrderId)}
     */
    @Test
    @Disabled
    public void getOrderWhenOrderNotFound() throws Exception {
    }
}
