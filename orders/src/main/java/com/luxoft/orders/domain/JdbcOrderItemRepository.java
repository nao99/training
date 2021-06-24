package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.persistent.DataAccessException;
import com.luxoft.orders.persistent.DatabaseException;
import com.luxoft.orders.persistent.query.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcOrderItemRepository class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
public class JdbcOrderItemRepository implements OrderItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcOrderItemRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<OrderItem> findById(Connection connection, Long id) throws DataAccessException {
        var sql =
            "SELECT id, ordering_id, item_name, item_count, item_price " +
            "FROM ordering_items " +
            "WHERE id = ? " +
            "FOR UPDATE;";

        try {
            return jdbcTemplate.select(connection, sql, List.of(id), rs -> {
                try {
                    if (!rs.next()) {
                        return null;
                    }

                    return assemblyOrderItemFromResultSet(rs);
                } catch (SQLException e) {
                    var errorMessage = String.format("Unable to find an order item: \"%s\"", e.getMessage());
                    logger.error(errorMessage);

                    throw new DataAccessException(errorMessage, e);
                }
            });
        } catch (DatabaseException e) {
            var errorMessage = String.format("Unable to find an order item: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    @Override
    public List<OrderItem> findByOrderId(Connection connection, Long orderId) throws DataAccessException {
        var sql = "SELECT id, ordering_id, item_name, item_count, item_price FROM ordering_items WHERE ordering_id = ?;";
        return jdbcTemplate.select(connection, sql, List.of(orderId), rs -> {
            try {
                List<OrderItem> itemsList = new ArrayList<>();
                while (rs.next()) {
                    var orderItem = assemblyOrderItemFromResultSet(rs);
                    itemsList.add(orderItem);
                }

                return itemsList;
            } catch (SQLException e) {
                var errorMessage = String.format("Unable to find order items: \"%s\"", e.getMessage());
                logger.error(errorMessage);

                throw new DataAccessException(errorMessage, e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public OrderItem save(Connection connection, OrderItem orderItem) throws DataAccessException {
        if (!isNew(orderItem)) {
            update(connection, orderItem);
            return orderItem;
        }

        return insert(connection, orderItem);
    }

    private boolean isNew(OrderItem item) {
        return item.getId() == null;
    }

    private OrderItem insert(Connection connection, OrderItem orderItem) throws DataAccessException {
        var sql = "INSERT INTO ordering_items (ordering_id, item_name, item_count, item_price) VALUES (?, ?, ?, ?);";
        List<Object> parameters = List.of(
            orderItem.getOrderId(),
            orderItem.getName(),
            orderItem.getCount(),
            orderItem.getPrice()
        );

        try {
            var orderItemId = jdbcTemplate.update(connection, sql, parameters);
            return orderItem.withId(orderItemId);
        } catch (DatabaseException e) {
            var errorMessage = String.format("Unable to creates an order item: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    private void update(Connection connection, OrderItem orderItem) throws DataAccessException {
        var sql = "UPDATE ordering_items SET item_count = ?, item_price = ? WHERE id = ?;";
        List<Object> parameters = List.of(
            orderItem.getCount(),
            orderItem.getPrice(),
            orderItem.getId()
        );

        try {
            jdbcTemplate.update(connection, sql, parameters);
        } catch (DatabaseException e) {
            var errorMessage = String.format("Unable to update an order item: \"%s\"", e.getMessage());
            logger.error(errorMessage);

            throw new DataAccessException(errorMessage, e);
        }
    }

    private OrderItem assemblyOrderItemFromResultSet(ResultSet rs) throws SQLException {
        return OrderItem.of(
            rs.getLong("id"),
            rs.getLong("ordering_id"),
            rs.getString("item_name"),
            rs.getInt("item_count"),
            rs.getBigDecimal("item_price")
        );
    }
}
