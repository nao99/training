package com.luxoft.orders.persistent.api;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.DataAccessException;
import com.luxoft.orders.persistent.DatabaseException;
import com.luxoft.orders.persistent.query.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JdbcOrderRepository class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
public class JdbcOrderRepository implements OrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcOrderRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final OrderItemRepository orderItemRepository;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate, OrderItemRepository orderItemRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Optional<Order> findById(Connection connection, Long id) throws DataAccessException {
        var sql = "SELECT id, user_name, done, updated_at FROM ordering WHERE id = ?;";
        try {
            return jdbcTemplate.select(connection, sql, List.of(id), rs -> {
                try {
                    if (!rs.next()) {
                        return null;
                    }

                    List<OrderItem> orderItems = orderItemRepository.findByOrderId(connection, rs.getLong("id"));

                    return assemblyOrderFromResultSet(rs, orderItems);
                } catch (SQLException e) {
                    var errorMessage = String.format("Unable to find an order: \"%s\"", e.getMessage());
                    logger.error(errorMessage);

                    throw new DataAccessException(errorMessage, e);
                }
            });
        } catch (DatabaseException e) {
            var errorMessage = String.format("Unable to find an order: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    @Override
    public boolean checkExistsByIdAndLock(Connection connection, Long id) throws DataAccessException {
        var sql = "SELECT EXISTS (SELECT 1 FROM ordering WHERE id = ? FOR UPDATE);";
        return jdbcTemplate.select(connection, sql, List.of(id), rs -> {
            try {
                rs.next();
                return rs.getBoolean(1);
            } catch (SQLException e) {
                var errorMessage = String.format("Unable to check an order existence: \"%s\"", e.getMessage());
                logger.error(errorMessage);

                throw new DataAccessException(errorMessage, e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected exception"));
    }

    @Override
    public Order save(Connection connection, Order order) throws DataAccessException {
        if (!isNew(order)) {
            update(connection, order);
            for (var orderItem : order.getItems()) {
                orderItemRepository.save(connection, orderItem);
            }

            return order;
        }

        var createdOrder = insert(connection, order);
        var orderItems = createdOrder.getItems();

        for (int i = 0; i < orderItems.size(); i++) {
            var orderItem = orderItems.get(i);
            var orderItemWithOrderId = orderItem.withOrderId(createdOrder.getId());

            var createdOrderItem = orderItemRepository.save(connection, orderItemWithOrderId);
            orderItems.set(i, createdOrderItem);
        }

        return createdOrder;
    }

    @Override
    public void updateOrderTimestamp(Connection connection, Long orderId) throws DataAccessException {
        var sql = "UPDATE ordering SET updated_at = ? WHERE id = ?;";
        var now = LocalDateTime.now();

        jdbcTemplate.update(connection, sql, List.of(now, orderId));
    }

    @Override
    public long countNonDone(Connection connection) throws DataAccessException {
        var sql = "SELECT COUNT(id) FROM ordering WHERE done = false;";
        return jdbcTemplate.select(connection, sql, List.of(), (rs) -> {
            try {
                if (!rs.next()) {
                    return null;
                }

                return rs.getLong(1);
            } catch (SQLException e) {
                var errorMessage = String.format("Unable to find a non done orders count: \"%s\"", e.getMessage());
                logger.error(errorMessage);

                throw new DataAccessException(errorMessage, e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public void doneAllNonDoneOrdersBatched(Connection connection, int batchSize) throws DataAccessException {
        var sql =
            "WITH non_done_orders AS (" +
                "SELECT id " +
                "FROM ordering " +
                "WHERE done = false " +
                    "FOR UPDATE " +
                        "SKIP LOCKED " +
                    "LIMIT ? " +
                ")" +
            "UPDATE ordering " +
            "SET done = true, updated_at = NOW() " +
            "FROM non_done_orders " +
            "WHERE ordering.id = non_done_orders.id;";

        jdbcTemplate.update(connection, sql, List.of(batchSize));
    }

    private boolean isNew(Order order) {
        return order.getId() == null;
    }

    private Order insert(Connection connection, Order order) throws DataAccessException {
        var sql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";
        List<Object> parameters = List.of(order.getUsername(), order.isDone(), order.getUpdatedAt());

        try {
            var orderId = jdbcTemplate.update(connection, sql, parameters);
            return order.withId(orderId);
        } catch (DatabaseException e) {
            var errorMessage = String.format("Unable to creates an order: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    private void update(Connection connection, Order order) throws DataAccessException {
        var sql = "UPDATE ordering SET done = ?, updated_at = ? WHERE id = ?;";
        List<Object> parameters = List.of(order.isDone(), order.getUpdatedAt(), order.getId());

        try {
            jdbcTemplate.update(connection, sql, parameters);
        } catch (DatabaseException e) {
            var errorMessage = String.format("Unable to update an order: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    private Order assemblyOrderFromResultSet(ResultSet rs, List<OrderItem> orderItems) throws SQLException {
        return Order.builder()
            .id(rs.getLong("id"))
            .username(rs.getString("user_name"))
            .done(rs.getBoolean("done"))
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .items(orderItems)
            .build();
    }
}
