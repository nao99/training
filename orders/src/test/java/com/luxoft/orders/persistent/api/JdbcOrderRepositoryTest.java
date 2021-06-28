package com.luxoft.orders.persistent.api;

import com.luxoft.orders.PostgreSQLContainerShared;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.DatabaseException;
import com.luxoft.orders.persistent.query.JdbcTemplate;
import com.luxoft.orders.persistent.query.JdbcTemplateImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JdbcOrderRepositoryTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
class JdbcOrderRepositoryTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER =
        PostgreSQLContainerShared.getInstance();

    static {
        POSTGRESQL_CONTAINER.start();
    }

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private JdbcOrderItemRepository orderItemRepositoryMock;
    private JdbcOrderRepository orderRepository;

    @BeforeEach
    public void setUp() throws Exception {
        var hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        hikariDataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        hikariDataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        hikariDataSource.setMaximumPoolSize(2);

        dataSource = hikariDataSource;
        jdbcTemplate = new JdbcTemplateImpl();
        orderItemRepositoryMock = mock(JdbcOrderItemRepository.class);
        orderRepository = new JdbcOrderRepository(jdbcTemplate, orderItemRepositoryMock);
    }

    @Test
    public void findByIdWhenOrderExists() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var connection = dataSource.getConnection()) {
            var createdOrder = orderRepository.save(connection, order);

            // when / then
            var selectedOrderOptional = orderRepository.findById(connection, createdOrder.getId());
            assertTrue(selectedOrderOptional.isPresent());

            var selectedOrderId = selectedOrderOptional.get().getId();

            verify(orderItemRepositoryMock, times(1))
                .findByOrderId(connection, selectedOrderId);

            assertEquals(createdOrder.getId(), selectedOrderOptional.get().getId());
        }
    }

    @Test
    public void findByIdWhenOrderNotExists() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            // when
            var selectedOrderOptional = orderRepository.findById(connection, -1L);

            verify(orderItemRepositoryMock, never())
                .findByOrderId(connection, eq(anyLong()));

            // then
            assertTrue(selectedOrderOptional.isEmpty());
        }
    }

    @Test
    public void existsByIdWhenOrderExists() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var connection = dataSource.getConnection()) {
            var createdOrder = orderRepository.save(connection, order);

            // when
            var orderExists = orderRepository.checkExistsByIdAndLock(connection, createdOrder.getId());

            // then
            assertTrue(orderExists);
        }
    }

    @Test
    public void existsByIdWhenOrderNotExists() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            // when
            var orderExists = orderRepository.checkExistsByIdAndLock(connection, -1L);

            // then
            assertFalse(orderExists);
        }
    }

    @Test
    public void existsByIdWhenOrderExistsAndTryToDelete() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var connection = dataSource.getConnection()) {
            var createdOrder = orderRepository.save(connection, order);

            // when / then
            var orderExists = orderRepository.checkExistsByIdAndLock(connection, createdOrder.getId());

            assertTrue(orderExists);
            assertThrows(
                DatabaseException.class,
                () -> jdbcTemplate.update(connection, "DELETE ordering WHERE id = ?;", List.of(createdOrder.getId()))
            );
        }
    }

    @Test
    public void saveWhenOrderIsNew() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        var orderItem = OrderItem.builder()
            .name("Shoes")
            .count(10)
            .price(BigDecimal.valueOf(500L))
            .build();

        var expectedOrderItem = OrderItem.builder()
            .id(1L)
            .orderId(1L)
            .name("Shoes")
            .count(10)
            .price(BigDecimal.valueOf(500L))
            .build();

        order.addItem(orderItem);

        try (var connection = dataSource.getConnection()) {
            // when / then
            when(orderItemRepositoryMock.save(eq(connection), any(OrderItem.class)))
                .thenReturn(expectedOrderItem);

            var createdOrder = orderRepository.save(connection, order);

            verify(orderItemRepositoryMock, times(1))
                .save(eq(connection), any(OrderItem.class));

            var createdOrderItem = createdOrder.getItems().get(0);

            assertNotNull(createdOrder.getId());
            assertNotNull(createdOrderItem.getOrderId());

            var selectedOrderOptional = orderRepository.findById(connection, createdOrder.getId());
            assertTrue(selectedOrderOptional.isPresent());

            var selectedOrder = selectedOrderOptional.get();

            assertNotNull(selectedOrder.getId());
            assertEquals(order.getUsername(), selectedOrder.getUsername());
            assertEquals(order.isDone(), selectedOrder.isDone());
            assertEquals(order.getUpdatedAt(), selectedOrder.getUpdatedAt());
        }
    }

    @Test
    public void saveWhenOrderIsNotNew() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        var orderItem = OrderItem.builder()
            .name("Shoes")
            .count(10)
            .price(BigDecimal.valueOf(500L))
            .build();

        order.addItem(orderItem);

        try (var connection = dataSource.getConnection()) {
            // when
            var createdOrder = orderRepository.save(connection, order);

            var oldOrderId = createdOrder.getId();
            var oldOrderUsername = createdOrder.getUsername();
            var oldOrderDone = createdOrder.isDone();
            var oldOrderUpdatedAt = createdOrder.getUpdatedAt();

            createdOrder.done();

            var updatedOrder = orderRepository.save(connection, createdOrder);

            // then
            assertEquals(oldOrderId, updatedOrder.getId());
            assertEquals(oldOrderUsername, updatedOrder.getUsername());
            assertNotEquals(oldOrderDone, updatedOrder.isDone());
            assertNotEquals(oldOrderUpdatedAt, updatedOrder.getUpdatedAt());
        }
    }

    @Test
    public void updateOrderTimestamp() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var connection = dataSource.getConnection()) {
            var createdOrder = orderRepository.save(connection, order);
            var orderUpdatedAt = order.getUpdatedAt();

            // when / then
            orderRepository.updateOrderTimestamp(connection, createdOrder.getId());
            var selectedOrder = orderRepository.findById(connection, createdOrder.getId());

            assertTrue(selectedOrder.isPresent());
            assertTrue(selectedOrder.get().getUpdatedAt().isAfter(orderUpdatedAt));
        }
    }

    @Test
    public void countNonDone() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        try (var connection = dataSource.getConnection()) {
            orderRepository.save(connection, order);

            // when
            var orderCount = orderRepository.countNonDone(connection);

            // then
            assertTrue(orderCount > 0L);
        }
    }

    @Test
    public void doneAllNonDoneOrdersBatched() throws Exception {
        // given
        var order = Order.builder()
            .username("Alex")
            .build();

        // when
        try (var connection = dataSource.getConnection()) {
            var createdOrder = orderRepository.save(connection, order);

            // when
            orderRepository.doneAllNonDoneOrdersBatched(connection, 15);

            var selectedOrder = orderRepository.findById(connection, createdOrder.getId()).orElseThrow();

            // then
            assertTrue(selectedOrder.isDone());
        }
    }
}
