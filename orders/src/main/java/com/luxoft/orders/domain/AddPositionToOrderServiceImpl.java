package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.infrastructure.transaction.TransactionRunner;

/**
 * AddPositionToOrderServiceImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
public class AddPositionToOrderServiceImpl implements AddPositionToOrderService {
    /**
     * Transaction runner
     */
    private final TransactionRunner transactionRunner;

    /**
     * Order repository
     */
    private final OrderRepository orderRepository;

    /**
     * Order item repository
     */
    private final OrderItemRepository orderItemRepository;

    /**
     * AddPositionToOrderServiceImpl constructor
     *
     * @param transactionRunner   a transaction runner
     * @param repository          an order repository
     * @param orderItemRepository an order item repository
     */
    public AddPositionToOrderServiceImpl(
        TransactionRunner transactionRunner,
        OrderRepository repository,
        OrderItemRepository orderItemRepository
    ) {
        this.transactionRunner = transactionRunner;
        this.orderRepository = repository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPosition(Order order, OrderItem orderItem) throws OrderPositionExistsException {
    }
}
