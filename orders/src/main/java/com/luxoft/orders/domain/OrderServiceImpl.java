package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.api.OrderItemRepository;
import com.luxoft.orders.persistent.api.OrderRepository;
import com.luxoft.orders.persistent.transaction.TransactionRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OrderServiceImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

        return transactionRunner.run(connection -> {
            var createdOrder = orderRepository.save(connection, order);
            logger.info("Created order with {} id", createdOrder.getId());

            return createdOrder;
        });
    }

    @Override
    public void addOrderItem(Long orderId, CreateOrderItemDto createOrderItemDto) throws OrderNotFoundException {
        transactionRunner.run(connection -> {
            var orderExists = orderRepository.checkExistsByIdAndLock(connection, orderId);
            if (!orderExists) {
                var errorMessage = String.format("Order %d was not found", orderId);
                logger.error(errorMessage);

                throw new OrderNotFoundException(errorMessage);
            }

            var orderItem = OrderItem.builder()
                .orderId(orderId)
                .name(createOrderItemDto.getName())
                .count(createOrderItemDto.getCount())
                .price(createOrderItemDto.getPrice())
                .build();

            var addedOrderItem = orderItemRepository.save(connection, orderItem);
            logger.info("Order item with {} id was added to order with {} id", addedOrderItem.getId(), orderId);

            orderRepository.updateOrderTimestamp(connection, orderId);

            return addedOrderItem;
        });
    }

    @Override
    public void changeOrderItemCount(Long orderItemId, int count) throws OrderItemNotFoundException {
        transactionRunner.run(connection -> {
            var orderItem = orderItemRepository.findByIdAndLock(connection, orderItemId)
                .orElseThrow(() -> {
                    var errorMessage = String.format("Order item %d was not found", orderItemId);
                    logger.error(errorMessage);

                    throw new OrderItemNotFoundException(errorMessage);
                });

            var previousOrderItemCount = orderItem.getCount();
            orderItem.changeCount(count);

            var changedOrderItem = orderItemRepository.save(connection, orderItem);

            var logMessagePattern = "Count of order item with {} id was changed from {} to {}";
            logger.info(logMessagePattern, orderItemId, previousOrderItemCount, count);

            orderRepository.updateOrderTimestamp(connection, orderItem.getOrderId());

            return changedOrderItem;
        });
    }

    @Override
    public void doneAllOrders() {
        transactionRunner.run(connection -> {
            var ordersCount = orderRepository.countNonDone(connection);
            if (ordersCount == 0) {
                logger.info("All orders have already been done");
                return 0;
            }

            var ordersDoneCount = 0L;
            var batchSize = 100;

            while (ordersDoneCount < ordersCount) {
                orderRepository.doneAllNonDoneOrdersBatched(connection, batchSize);
                ordersDoneCount += batchSize;
            }

            logger.info("All orders were successfully done");

            return ordersDoneCount;
        });
    }
}
