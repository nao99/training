package com.luxoft.orders.domain;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.DataAccessException;
import com.luxoft.orders.persistent.api.OrderItemRepository;
import com.luxoft.orders.persistent.api.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * OrderServiceImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-22
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(String.format("Order %d was not found", id)));
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderDto createOrderDto) {
        var order = Order.builder()
            .username(createOrderDto.getUsername())
            .build();

        orderRepository.save(order);

        for (var createOrderItemDto : createOrderDto.getItems()) {
            var orderItem = OrderItem.builder()
                .name(createOrderItemDto.getName())
                .order(order)
                .count(createOrderItemDto.getCount())
                .price(createOrderItemDto.getPrice())
                .build();

            order.addItem(orderItem);
            orderItemRepository.save(orderItem);
        }

        logger.info("Created order with {} id", order.getId());

        return order;
    }

    @Override
    @Transactional(rollbackFor = OrderNotFoundException.class)
    public OrderItem addOrderItem(Long orderId, CreateOrderItemDto createOrderItemDto) throws OrderNotFoundException {
        var orderOptional = orderRepository.findByIdAndLock(orderId);
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

        orderItemRepository.save(orderItem);
        orderRepository.save(order);

        logger.info("Order item with {} id was added to order with {} id", orderItem.getId(), orderId);

        return orderItem;
    }

    @Override
    @Transactional(rollbackFor = OrderItemNotFoundException.class)
    public void changeOrderItemCount(Long orderItemId, int count) throws OrderItemNotFoundException {
        var orderItem = orderItemRepository.findByIdAndLock(orderItemId)
            .orElseThrow(() -> {
                var errorMessage = String.format("Order item %d was not found", orderItemId);
                logger.error(errorMessage);

                throw new OrderItemNotFoundException(errorMessage);
            });

        var previousOrderItemCount = orderItem.getCount();
        orderItem.changeCount(count);

        orderItemRepository.save(orderItem);

        var logMessagePattern = "Count of order item with {} id was changed from {} to {}";
        logger.info(logMessagePattern, orderItemId, previousOrderItemCount, count);

        orderRepository.updateOrderTimestamp(orderItem.getOrder().getId(), LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public void doneAllOrders() {
        var ordersCount = orderRepository.countNonDone();
        if (ordersCount == 0) {
            logger.info("All orders have already been done");
            return;
        }

        var ordersDoneCount = 0L;
        var batchSize = 100;

        while (ordersDoneCount < ordersCount) {
            orderRepository.doneAllNonDoneOrdersBatched(batchSize);
            ordersDoneCount += batchSize;
        }

        logger.info("All orders were successfully done");
    }
}
