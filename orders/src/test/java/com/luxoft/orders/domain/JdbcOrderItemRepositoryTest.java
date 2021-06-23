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
import java.sql.Connection;
import java.sql.SQLException;
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
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER
        = PostgreSQLContainerShared.getInstance();

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

            var selectedOrderItemOrderId = jdbcTemplate.select(
                connection,
                "SELECT ordering_id FROM ordering_items WHERE id = ?;",
                List.of(createdOrderItem.getId()),
                (rs) -> {
                    try {
                        rs.next();
                        return rs.getLong("ordering_id");
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            );

            assertTrue(selectedOrderItemOrderId.isPresent());
            assertEquals(order.getId(), selectedOrderItemOrderId.get());
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

            orderItem.changeCount(15);

            var updatedOrderItem = repository.save(connection, orderItem);

            // when / then
            var selectedOrderItemOptional = jdbcTemplate.select(
                connection,
                "SELECT id, ordering_id, item_name, item_count, item_price FROM ordering_items WHERE id = ?;",
                List.of(updatedOrderItem.getId()),
                (rs) -> {
                    try {
                        rs.next();
                        return OrderItem.of(
                            rs.getLong("id"),
                            rs.getLong("ordering_id"),
                            rs.getString("item_name"),
                            rs.getInt("item_count"),
                            rs.getBigDecimal("item_price")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            );

            assertTrue(selectedOrderItemOptional.isPresent());

            OrderItem selectedOrderItem = selectedOrderItemOptional.get();

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

        order.setId(orderId);

        return order;
    }
}
