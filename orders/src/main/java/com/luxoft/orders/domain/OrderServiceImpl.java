package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.transaction.TransactionRunner;

/**
 * OrderServiceImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public class OrderServiceImpl implements OrderService {
    private final TransactionRunner transactionRunner;
    private final OrderRepository repository;

    public OrderServiceImpl(TransactionRunner transactionRunner, OrderRepository repository) {
        this.transactionRunner = transactionRunner;
        this.repository = repository;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        return transactionRunner.run(connection -> repository.findById(connection, id)
            .orElseThrow(() -> new OrderNotFoundException(String.format("Order %d id not found", id)))
        );
    }

    @Override
    public Order createOrder(CreateOrderDto createOrderDto) {
        var order = Order.of(createOrderDto.getUsername());
        createOrderDto.getItems()
            .forEach(item -> order.addItem(OrderItem.of(item.getName(), item.getCount(), item.getPrice())));

        return transactionRunner.run(connection -> repository.save(connection, order));
    }

    @Override
    public void addOrderItem(Order order, CreateOrderItemDto createOrderItemDto) {
        var orderItem = OrderItem.of(
            order.getId(),
            createOrderItemDto.getName(),
            createOrderItemDto.getCount(),
            createOrderItemDto.getPrice()
        );

        order.addItem(orderItem);
        transactionRunner.run(connection -> repository.save(connection, order));
    }

    @Override
    public void changeOrderItemCount(OrderItem orderItem, int count) {

    }

    @Override
    public void doneAllOrders() {
        transactionRunner.run(connection -> {
            var ordersCount = repository.countNonDone(connection);
            if (ordersCount == 0) {
                return 0;
            }

            var ordersDoneCount = 0L;
            var batchSize = 100;

            while (ordersDoneCount < ordersCount) {
                repository.doneAllNonDoneOrdersBatched(connection, batchSize);
                ordersDoneCount += batchSize;
            }

            return ordersDoneCount;
        });
    }
}
