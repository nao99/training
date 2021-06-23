package com.luxoft.orders.domain;

import com.luxoft.orders.PostgreSQLContainerShared;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.query.JdbcTemplate;
import com.luxoft.orders.persistent.query.JdbcTemplateImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
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
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER
        = PostgreSQLContainerShared.getInstance();

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

        dataSource = hikariDataSource;
        jdbcTemplate = new JdbcTemplateImpl();
        orderItemRepositoryMock = mock(JdbcOrderItemRepository.class);
        orderRepository = new JdbcOrderRepository(jdbcTemplate, orderItemRepositoryMock);
    }

    @Test
    public void findByIdWhenOrderExists() throws Exception {
        // given
        var order = Order.of("Alex");

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
    public void saveWhenOrderIsNew() throws Exception {
        // given
        var order = Order.of("Alex");
        var orderItem = OrderItem.of("Shoes", 10, BigDecimal.valueOf(500L));
        var expectedOrderItem = OrderItem.of(1L, 1L, "Shoes", 10, BigDecimal.valueOf(500L));

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

            var selectedOrderUsernameOptional = jdbcTemplate.select(
                connection,
                "SELECT id, user_name, done, updated_at FROM ordering WHERE id = ?;",
                List.of(createdOrder.getId()),
                (rs) -> {
                    try {
                        rs.next();
                        return rs.getString("user_name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            );

            assertTrue(selectedOrderUsernameOptional.isPresent());
            assertEquals(order.getUsername(), selectedOrderUsernameOptional.get());
        }
    }

    @Test
    public void saveWhenOrderIsNotNew() throws Exception {
        // given
        var order = Order.of("Alex");
        var orderItem = OrderItem.of("Shoes", 10, BigDecimal.valueOf(500L));

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
    public void countNonDone() throws Exception {
        // given
        var order1 = Order.of("Alex");
        try (var connection = dataSource.getConnection()) {
            orderRepository.save(connection, order1);

            // when
            var orderCount = orderRepository.countNonDone(connection);

            // then
            assertTrue(orderCount > 0L);
        }
    }

    @Test
    public void doneAllNonDoneOrdersBatched() throws Exception {
        // given
        var order1 = Order.of("Alex");
        var order2 = Order.of("Petr");
        var order3 = Order.of("Artur");

        // when
        try (var connection = dataSource.getConnection()) {
            var createdOrder1 = orderRepository.save(connection, order1);
            var createdOrder2 = orderRepository.save(connection, order2);
            var createdOrder3 = orderRepository.save(connection, order3);

            // when
            orderRepository.doneAllNonDoneOrdersBatched(connection, 15);

            var selectedOrder1 = orderRepository.findById(connection, createdOrder1.getId()).orElseThrow();
            var selectedOrder2 = orderRepository.findById(connection, createdOrder2.getId()).orElseThrow();
            var selectedOrder3 = orderRepository.findById(connection, createdOrder3.getId()).orElseThrow();

            // then
            assertTrue(selectedOrder1.isDone());
            assertTrue(selectedOrder2.isDone());
            assertTrue(selectedOrder3.isDone());
        }
    }
}
