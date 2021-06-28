package com.luxoft.orders.domain;

import com.luxoft.orders.PostgreSQLContainerShared;
import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.api.JdbcOrderItemRepository;
import com.luxoft.orders.persistent.api.JdbcOrderRepository;
import com.luxoft.orders.persistent.query.JdbcTemplateImpl;
import com.luxoft.orders.persistent.transaction.JdbcTransactionRunner;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderServiceImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-24
 */
class OrderServiceImplTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER =
        PostgreSQLContainerShared.getInstance();

    static {
        POSTGRESQL_CONTAINER.start();
    }

    private DataSource dataSource;
    private JdbcOrderItemRepository orderItemRepository;
    private JdbcOrderRepository orderRepository;

    private OrderServiceImpl service;

    @BeforeEach
    public void setUp() throws Exception {
        var hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        hikariDataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        hikariDataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        hikariDataSource.setMaximumPoolSize(2);
        hikariDataSource.setAutoCommit(false);

        dataSource = hikariDataSource;

        var transactionRunner = new JdbcTransactionRunner(dataSource);
        var jdbcTemplate = new JdbcTemplateImpl();

        orderItemRepository = new JdbcOrderItemRepository(jdbcTemplate);
        orderRepository = new JdbcOrderRepository(jdbcTemplate, orderItemRepository);

        service = new OrderServiceImpl(transactionRunner, orderRepository, orderItemRepository);
    }

    @Test
    public void getOrderWhenOrderExists() throws Exception {
        // given
        var order = createOrder("Alex");

        // when
        var orderResult = service.getOrder(order.getId());

        // then
        assertEquals(order.getId(), orderResult.getId());
        assertEquals(order.getUsername(), orderResult.getUsername());
        assertEquals(order.isDone(), orderResult.isDone());
        assertEquals(order.getUpdatedAt(), orderResult.getUpdatedAt());
        assertEquals(order.getItems(), orderResult.getItems());
    }

    @Test
    public void getOrderWhenOrderNotExists() throws Exception {
        // when / then
        assertThrows(OrderNotFoundException.class, () -> service.getOrder(-1L));
    }

    @Test
    public void createOrder() throws Exception {
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
        assertNotNull(createdOrder.getId());
        assertNotNull(createdOrder.getUpdatedAt());
        assertFalse(createdOrder.isDone());

        assertEquals(createOrderDto.getUsername(), createdOrder.getUsername());
        assertEquals(1, createdOrder.getItems().size());

        var createdOrderItem = createdOrder.getItems().get(0);

        assertEquals(createOrderItemDto.getName(), createdOrderItem.getName());
        assertEquals(createOrderItemDto.getCount(), createdOrderItem.getCount());
        assertEquals(createOrderItemDto.getPrice(), createdOrderItem.getPrice());
    }

    @Test
    public void addOrderItemWhenOrderExists() throws Exception {
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
        assertEquals(1, selectedOrder.getItems().size());

        var selectedOrderItem = selectedOrder.getItems().get(0);

        assertEquals(createOrderItemDto.getName(), selectedOrderItem.getName());
        assertEquals(createOrderItemDto.getCount(), selectedOrderItem.getCount());

        assertEquals(0, selectedOrderItem.getPrice().compareTo(createOrderItemDto.getPrice()));
    }

    @Test
    public void addOrderItemWhenOrderNotExists() throws Exception {
        // given
        var createOrderItemDto = new CreateOrderItemDto();

        createOrderItemDto.setName("Shoes");
        createOrderItemDto.setCount(4);
        createOrderItemDto.setPrice(BigDecimal.valueOf(5000L));

        // when / then
        assertThrows(OrderNotFoundException.class, () -> service.addOrderItem(-1L, createOrderItemDto));
    }

    @Test
    public void changeOrderItemCountWhenOrderItemExists() throws Exception {
        // given
        var expectedCount = 15;

        var order = createOrder("Alex");
        var orderItem = createOrderItem(order.getId(), "Shoes", 10, BigDecimal.valueOf(1000L));

        // when
        service.changeOrderItemCount(orderItem.getId(), expectedCount);

        var selectedOrder = service.getOrder(order.getId());
        var selectedOrderItem = selectedOrder.getItems().get(0);

        // then
        assertEquals(orderItem.getId(), selectedOrderItem.getId());
        assertEquals(orderItem.getOrderId(), selectedOrderItem.getOrderId());
        assertEquals(orderItem.getName(), selectedOrderItem.getName());
        assertEquals(expectedCount, selectedOrderItem.getCount());
        assertEquals(0, selectedOrderItem.getPrice().compareTo(orderItem.getPrice()));
    }

    @Test
    public void changeOrderItemCountWhenOrderItemNotExists() throws Exception {
        // when / then
        assertThrows(OrderItemNotFoundException.class, () -> service.changeOrderItemCount(-1L, 15));
    }

    @Test
    public void doneAllOrders() throws Exception {
        // given
        var order = createOrder("Alex");

        // when
        service.doneAllOrders();

        var selectedOrder = service.getOrder(order.getId());

        // then
        assertFalse(order.isDone());
        assertTrue(selectedOrder.isDone());
    }

    /**
     * Creates an {@link Order} for testing purposes
     * Created order will be saved in a db
     *
     * @param username an order username
     *
     * @return a saved order
     * @throws SQLException if unable to commit or open connection
     */
    private Order createOrder(String username) throws SQLException {
        Order order;
        try (var connection = dataSource.getConnection()) {
            var orderToSave = Order.builder()
                .username(username)
                .build();

            order = orderRepository.save(connection, orderToSave);
            connection.commit();
        }

        return order;
    }

    /**
     * Creates an {@link OrderItem} for testing purposes
     * Created order item will be saved in a db
     *
     * @param orderId an order id
     * @param name    a name
     * @param count   a count
     * @param price   a price
     *
     * @return a saved order item
     * @throws SQLException if unable to commit or open connection
     */
    private OrderItem createOrderItem(Long orderId, String name, int count, BigDecimal price) throws SQLException {
        OrderItem orderItem;
        try (var connection = dataSource.getConnection()) {
            var orderItemToSave = OrderItem.builder()
                .orderId(orderId)
                .name(name)
                .count(count)
                .price(price)
                .build();

            orderItem = orderItemRepository.save(connection, orderItemToSave);
            connection.commit();
        }

        return orderItem;
    }
}
