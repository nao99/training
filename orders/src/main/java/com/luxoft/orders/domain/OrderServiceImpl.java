package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.api.OrderItemRepository;
import com.luxoft.orders.persistent.api.OrderRepository;
import com.luxoft.orders.persistent.transaction.TransactionRunner;
import org.hibernate.LockMode;
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
        return transactionRunner.run(session -> orderRepository.findById(session, id, LockMode.NONE)
            .orElseThrow(() -> new OrderNotFoundException(String.format("Order %d was not found", id)))
        );
    }

    @Override
    public Order createOrder(CreateOrderDto createOrderDto) {
        var order = Order.builder()
            .username(createOrderDto.getUsername())
            .build();

        return transactionRunner.run(session -> {
            orderRepository.save(session, order);

            for (var createOrderItemDto : createOrderDto.getItems()) {
                var orderItem = OrderItem.builder()
                    .name(createOrderItemDto.getName())
                    .order(order)
                    .count(createOrderItemDto.getCount())
                    .price(createOrderItemDto.getPrice())
                    .build();

                order.addItem(orderItem);
                orderItemRepository.save(session, orderItem);
            }

            logger.info("Created order with {} id", order.getId());

            return order;
        });
    }

    @Override
    public void addOrderItem(Long orderId, CreateOrderItemDto createOrderItemDto) throws OrderNotFoundException {
        transactionRunner.run(session -> {
            var orderOptional = orderRepository.findById(session, orderId, LockMode.PESSIMISTIC_READ);
            if (orderOptional.isEmpty()) {
                var errorMessage = String.format("Order %d was not found", orderId);
                logger.error(errorMessage);

                throw new OrderNotFoundException(errorMessage);
            }

            var order = orderOptional.get();
            var orderItem = OrderItem.builder()
                .order(order)
                .name(createOrderItemDto.getName())
                .count(createOrderItemDto.getCount())
                .price(createOrderItemDto.getPrice())
                .build();

            order.updateTimestamp();

            orderItemRepository.save(session, orderItem);
            orderRepository.save(session, order);

            logger.info("Order item with {} id was added to order with {} id", orderItem.getId(), orderId);

            return orderItem;
        });
    }

    @Override
    public void changeOrderItemCount(Long orderItemId, int count) throws OrderItemNotFoundException {
        transactionRunner.run(session -> {
            var orderItem = orderItemRepository.findById(session, orderItemId, LockMode.PESSIMISTIC_READ)
                .orElseThrow(() -> {
                    var errorMessage = String.format("Order item %d was not found", orderItemId);
                    logger.error(errorMessage);

                    throw new OrderItemNotFoundException(errorMessage);
                });

            var previousOrderItemCount = orderItem.getCount();
            orderItem.changeCount(count);

            orderItemRepository.save(session, orderItem);

            var logMessagePattern = "Count of order item with {} id was changed from {} to {}";
            logger.info(logMessagePattern, orderItemId, previousOrderItemCount, count);

            orderRepository.updateOrderTimestamp(session, orderItem.getOrder().getId());

            return orderItem;
        });
    }

    @Override
    public void doneAllOrders() {
        transactionRunner.run(session -> {
            var ordersCount = orderRepository.countNonDone(session);
            if (ordersCount == 0) {
                logger.info("All orders have already been done");
                return 0;
            }

            var ordersDoneCount = 0L;
            var batchSize = 100;

            while (ordersDoneCount < ordersCount) {
                orderRepository.doneAllNonDoneOrdersBatched(session, batchSize);
                ordersDoneCount += batchSize;
            }

            logger.info("All orders were successfully done");

            return ordersDoneCount;
        });
    }
}
