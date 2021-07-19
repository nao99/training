package com.luxoft.orders.domain;

import com.luxoft.orders.DatabaseTest;
import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.api.JpaOrderItemRepository;
import com.luxoft.orders.persistent.api.JpaOrderRepository;
import com.luxoft.orders.persistent.transaction.JpaTransactionRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * OrderServiceImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-24
 */
class OrderServiceImplTest extends DatabaseTest {
    private JpaOrderItemRepository orderItemRepository;
    private JpaOrderRepository orderRepository;

    private OrderServiceImpl service;

    @BeforeEach
    public void setUp() throws Exception {
        var transactionRunner = new JpaTransactionRunner(sessionFactory);

        orderItemRepository = new JpaOrderItemRepository();
        orderRepository = new JpaOrderRepository();

        service = new OrderServiceImpl(transactionRunner, orderRepository, orderItemRepository);
    }

    @Test
    void getOrderWhenOrderExists() throws Exception {
        // given
        var order = createOrder("Alex");

        // when
        var orderResult = service.getOrder(order.getId());

        // then
        assertThat(orderResult.getId())
            .isEqualTo(order.getId());

        assertThat(orderResult.getUsername())
            .isEqualTo(order.getUsername());

        assertThat(orderResult.isDone())
            .isEqualTo(order.isDone());
    }

    @Test
    void getOrderWhenOrderNotExists() throws Exception {
        // when / then
        assertThrows(OrderNotFoundException.class, () -> service.getOrder(-1L));
    }

    @Test
    void createOrder() throws Exception {
        // given
        var createOrderItemDto = new CreateOrderItemDto();

        createOrderItemDto.setName("Shoes");
        createOrderItemDto.setCount(4);
        createOrderItemDto.setPrice(BigDecimal.valueOf(5000L));

        var createOrderDto = new CreateOrderDto();

        createOrderDto.setUsername("Alex");
        createOrderDto.setItems(List.of(createOrderItemDto));

        // when
        var createdOrder = service.createOrder(createOrderDto);

        // then
        assertThat(createdOrder.getId())
            .isNotNull();

        assertThat(createdOrder.getUpdatedAt())
            .isNotNull();

        assertThat(createdOrder.isDone())
            .isFalse();

        assertThat(createdOrder.getUsername())
            .isEqualTo(createOrderDto.getUsername());

        assertThat(createdOrder.getItems().size())
            .isEqualTo(1);

        var createdOrderItem = createdOrder.getItems().get(0);

        assertThat(createdOrderItem.getName())
            .isEqualTo(createOrderItemDto.getName());

        assertThat(createdOrderItem.getCount())
            .isEqualTo(createOrderItemDto.getCount());

        assertThat(createdOrderItem.getPrice())
            .isEqualTo(createOrderItemDto.getPrice());
    }

    @Test
    void addOrderItemWhenOrderExists() throws Exception {
        // given
        var order = createOrder("Alex");
        var createOrderItemDto = new CreateOrderItemDto();

        createOrderItemDto.setName("Shoes");
        createOrderItemDto.setCount(4);
        createOrderItemDto.setPrice(BigDecimal.valueOf(5000L));

        // when
        service.addOrderItem(order.getId(), createOrderItemDto);

        var selectedOrder = service.getOrder(order.getId());

        // then
        assertThat(selectedOrder.getItems().size())
            .isEqualTo(1);

        var selectedOrderItem = selectedOrder.getItems().get(0);

        assertThat(selectedOrderItem.getName())
            .isEqualTo(createOrderItemDto.getName());

        assertThat(selectedOrderItem.getCount())
            .isEqualTo(createOrderItemDto.getCount());

        assertThat(selectedOrderItem.getPrice().compareTo(createOrderItemDto.getPrice()))
            .isEqualTo(0);
    }

    @Test
    void addOrderItemWhenOrderNotExists() throws Exception {
        // given
        var createOrderItemDto = new CreateOrderItemDto();

        createOrderItemDto.setName("Shoes");
        createOrderItemDto.setCount(4);
        createOrderItemDto.setPrice(BigDecimal.valueOf(5000L));

        // when / then
        assertThrows(OrderNotFoundException.class, () -> service.addOrderItem(-1L, createOrderItemDto));
    }

    @Test
    void changeOrderItemCountWhenOrderItemExists() throws Exception {
        // given
        var expectedCount = 15;

        var order = createOrder("Alex");
        var orderItem = createOrderItem(order, "Shoes", 10, BigDecimal.valueOf(1000L));

        // when
        service.changeOrderItemCount(orderItem.getId(), expectedCount);

        var selectedOrder = service.getOrder(order.getId());
        var selectedOrderItem = selectedOrder.getItems().get(0);

        // then
        assertThat(selectedOrderItem.getId())
            .isEqualTo(orderItem.getId());

        assertThat(selectedOrderItem.getOrder().getId())
            .isEqualTo(orderItem.getOrder().getId());

        assertThat(selectedOrderItem.getName())
            .isEqualTo(orderItem.getName());

        assertThat(selectedOrderItem.getPrice())
            .isEqualByComparingTo(orderItem.getPrice());

        assertThat(selectedOrderItem.getCount())
            .isEqualTo(expectedCount);
    }

    @Test
    void changeOrderItemCountWhenOrderItemNotExists() throws Exception {
        // when / then
        assertThrows(OrderItemNotFoundException.class, () -> service.changeOrderItemCount(-1L, 15));
    }

    @Test
    void doneAllOrders() throws Exception {
        // given
        var order1 = createOrder("Alex");
        var order2 = createOrder("Alex");
        var order3 = createOrder("Alex");

        // when
        service.doneAllOrders();

        var selectedOrder1 = service.getOrder(order1.getId());
        var selectedOrder2 = service.getOrder(order2.getId());
        var selectedOrder3 = service.getOrder(order3.getId());

        // then
        assertThat(selectedOrder1.isDone())
            .isTrue();

        assertThat(selectedOrder2.isDone())
            .isTrue();

        assertThat(selectedOrder3.isDone())
            .isTrue();
    }

    /**
     * Creates an {@link Order} for testing purposes
     * Created order will be saved in a db
     *
     * @param username an order username
     * @return a created order
     */
    private Order createOrder(String username) {
        var order = Order.builder()
            .username(username)
            .build();

        try (var session = sessionFactory.openSession()) {
            var transaction = session.getTransaction();
            transaction.begin();

            orderRepository.save(session, order);
            transaction.commit();
        }

        return order;
    }

    /**
     * Creates an {@link OrderItem} for testing purposes
     * Created order item will be saved in a db
     *
     * @param order an order
     * @param name  a name
     * @param count a count
     * @param price a price
     *
     * @return a saved order item
     */
    private OrderItem createOrderItem(Order order, String name, int count, BigDecimal price) {
        var orderItem = OrderItem.builder()
            .order(order)
            .name(name)
            .count(count)
            .price(price)
            .build();

        try (var session = sessionFactory.openSession()) {
            var transaction = session.getTransaction();
            transaction.begin();

            orderItemRepository.save(session, orderItem);
            transaction.commit();
        }

        return orderItem;
    }
}
