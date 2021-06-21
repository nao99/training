package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderId;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.infrastructure.transaction.TransactionRunner;

import java.util.Optional;

/**
 * GetOrderServiceImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class GetOrderServiceImpl implements GetOrderService {
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
     * GetOrderServiceImpl constructor
     *
     * @param transactionRunner   a transaction runner
     * @param repository          an order repository
     * @param orderItemRepository an order item repository
     */
    public GetOrderServiceImpl(
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
    public Order getOrder(OrderId id) throws OrderNotFoundException {
        return transactionRunner.run(connection -> {
            Optional<Order> orderOptional = orderRepository.find(connection, id);
            if (orderOptional.isEmpty()) {
                return orderOptional;
            }

            Order order = orderOptional.get();
            Iterable<OrderItem> orderItems = orderItemRepository.findByOrderId(connection, order.getId());

            orderItems.forEach(order::addItem);
            return orderOptional;
        }).orElseThrow(() -> new OrderNotFoundException(String.format("Order with %d id was not found", id.id())));
    }
}
