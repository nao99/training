package com.luxoft.orders.domain;

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
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JdbcOrderItemRepositoryTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
class JdbcOrderItemRepositoryTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER =
        PostgreSQLContainerShared.getInstance();

    static {
        POSTGRESQL_CONTAINER.start();
    }

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private JdbcOrderItemRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        var hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        hikariDataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        hikariDataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        hikariDataSource.setMaximumPoolSize(2);

        dataSource = hikariDataSource;
        jdbcTemplate = new JdbcTemplateImpl();
        repository = new JdbcOrderItemRepository(jdbcTemplate);
    }

    @Test
    public void findByIdWhenOrderItemExists() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            var order = createOrder(connection);
            var orderItem = OrderItem.of(order.getId(), "Shoes", 12, BigDecimal.valueOf(1200L));
            var createdOrderItem = repository.save(connection, orderItem);

            // when
            var selectedOrderItemOptional = repository.findById(connection, createdOrderItem.getId());

            // then
            assertTrue(selectedOrderItemOptional.isPresent());
            assertEquals(createdOrderItem.getId(), selectedOrderItemOptional.get().getId());
        }
    }

    @Test
    public void findByIdWhenOrderItemNotExists() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            // when
            var selectedOrderItemOptional = repository.findById(connection, -1L);

            // then
            assertTrue(selectedOrderItemOptional.isEmpty());
        }
    }

    @Test
    public void findByIdAndTryToDelete() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            var order = createOrder(connection);
            var orderItem = OrderItem.of(order.getId(), "Shoes", 12, BigDecimal.valueOf(1200L));

            var createdOrderItem = repository.save(connection, orderItem);
            var orderItemId = createdOrderItem.getId();

            // when / then
            var selectedOrderItemOptional = repository.findById(connection, orderItemId);

            assertTrue(selectedOrderItemOptional.isPresent());
            assertThrows(
                DatabaseException.class,
                () -> jdbcTemplate.update(connection, "DELETE ordering_items WHERE id = ?;", List.of(orderItemId))
            );
        }
    }

    @Test
    public void findByOrderId() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            var order = createOrder(connection);
            var orderItem = OrderItem.of(order.getId(), "Shoes", 12, BigDecimal.valueOf(1200L));
            var createdOrderItem = repository.save(connection, orderItem);

            // when
            var selectedOrderItemOptional = repository.findByOrderId(connection, createdOrderItem.getOrderId());

            // then
            assertEquals(1, selectedOrderItemOptional.size());
            assertEquals(createdOrderItem.getId(), selectedOrderItemOptional.get(0).getId());
        }
    }

    @Test
    public void saveWhenOrderItemIsNew() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            var order = createOrder(connection);
            var orderItem = OrderItem.of(order.getId(), "Shoes", 12, BigDecimal.valueOf(1200L));

            // when / then
            var createdOrderItem = repository.save(connection, orderItem);
            assertNotNull(createdOrderItem.getId());

            var selectedOrderItemOptional = repository.findById(connection, createdOrderItem.getId());

            assertTrue(selectedOrderItemOptional.isPresent());
            assertEquals(createdOrderItem.getId(), selectedOrderItemOptional.get().getId());
        }
    }

    @Test
    public void saveWhenOrderItemIsNotNew() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            var order = createOrder(connection);
            var orderItem = OrderItem.of(order.getId(), "Shoes", 12, BigDecimal.valueOf(1200L));

            var createdOrderItem = repository.save(connection, orderItem);

            var oldOrderItemCount = createdOrderItem.getCount();
            var oldOrderItemPrice = createdOrderItem.getPrice();

            createdOrderItem.changeCount(15);

            var updatedOrderItem = repository.save(connection, createdOrderItem);

            // when / then
            var selectedOrderItemOptional = repository.findById(connection, updatedOrderItem.getId());
            assertTrue(selectedOrderItemOptional.isPresent());

            var selectedOrderItem = selectedOrderItemOptional.get();

            assertEquals(createdOrderItem.getId(), selectedOrderItem.getId());
            assertEquals(createdOrderItem.getOrderId(), selectedOrderItem.getOrderId());
            assertEquals(createdOrderItem.getName(), selectedOrderItem.getName());
            assertNotEquals(oldOrderItemCount, selectedOrderItem.getCount());
            assertNotEquals(oldOrderItemPrice, selectedOrderItem.getPrice());
        }
    }

    /**
     * Creates an {@link Order} for testing purposes
     * This order will be persisted in db
     *
     * @param connection a db connection
     * @return an order
     */
    private Order createOrder(Connection connection) {
        var order = Order.of("Alex");
        var orderId = jdbcTemplate.update(
            connection,
            "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);",
            List.of(order.getUsername(), order.isDone(), order.getUpdatedAt())
        );

        order = order.withId(orderId);

        return order;
    }
}
