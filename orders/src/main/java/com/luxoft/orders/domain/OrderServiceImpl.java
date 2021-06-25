package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.api.OrderItemRepository;
import com.luxoft.orders.persistent.api.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(
        TransactionRunner transactionRunner,
        OrderRepository orderRepository,
        OrderItemRepository orderItemRepository
    ) {
        this.transactionRunner = transactionRunner;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        return transactionRunner.run(connection -> orderRepository.findById(connection, id)
            .orElseThrow(() -> new OrderNotFoundException(String.format("Order %d was not found", id)))
        );
    }

    @Override
    public Order createOrder(CreateOrderDto createOrderDto) {
        var order = Order.builder()
            .username(createOrderDto.getUsername())
            .build();

        for (var createOrderItemDto : createOrderDto.getItems()) {
            var orderItem = OrderItem.builder()
                .name(createOrderItemDto.getName())
                .count(createOrderItemDto.getCount())
                .price(createOrderItemDto.getPrice())
                .build();

            order.addItem(orderItem);
        }

        return transactionRunner.run(connection -> orderRepository.save(connection, order));
    }

    @Override
    public void addOrderItem(Long orderId, CreateOrderItemDto createOrderItemDto) throws OrderNotFoundException {
        transactionRunner.run(connection -> {
            var orderExists = orderRepository.checkExistsByIdAndLock(connection, orderId);
            if (!orderExists) {
                throw new OrderNotFoundException(String.format("Order %d was not found", orderId));
            }

            var orderItem = OrderItem.builder()
                .orderId(orderId)
                .name(createOrderItemDto.getName())
                .count(createOrderItemDto.getCount())
                .price(createOrderItemDto.getPrice())
                .build();

            orderItemRepository.save(connection, orderItem);
            orderRepository.updateOrderTimestamp(connection, orderId);

            return orderItem;
        });
    }

    @Override
    public void changeOrderItemCount(Long orderItemId, int count) throws OrderItemNotFoundException {
        transactionRunner.run(connection -> {
            var orderItem = orderItemRepository.findByIdAndLock(connection, orderItemId)
                .orElseThrow(() -> {
                    throw new OrderItemNotFoundException(String.format("Order item %d was not found", orderItemId));
                });

            orderItem.changeCount(count);

            orderItemRepository.save(connection, orderItem);
            orderRepository.updateOrderTimestamp(connection, orderItem.getOrderId());

            return orderItem;
        });
    }

    @Override
    public void doneAllOrders() {
        transactionRunner.run(connection -> {
            var ordersCount = orderRepository.countNonDone(connection);
            if (ordersCount == 0) {
                return 0;
            }

            var ordersDoneCount = 0L;
            var batchSize = 100;

            while (ordersDoneCount < ordersCount) {
                orderRepository.doneAllNonDoneOrdersBatched(connection, batchSize);
                ordersDoneCount += batchSize;
            }

            return ordersDoneCount;
        });
    }
}
