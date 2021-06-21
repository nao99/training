package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.*;
import com.luxoft.orders.infrastructure.DataAccessException;
import com.luxoft.orders.infrastructure.DatabaseException;
import com.luxoft.orders.infrastructure.query.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * JdbcOrderRepository class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
public class JdbcOrderRepository implements OrderRepository {
    /**
     * JDBC template
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * JdbcOrderRepository constructor
     *
     * @param jdbcTemplate a jdbc template
     */
    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> find(Connection connection, OrderId orderId) throws DataAccessException {
        String sql = "SELECT id, user_name, done, updated_at FROM ordering WHERE id = ?;";
        try {
            return jdbcTemplate.select(connection, sql, List.of(orderId.id()), resultSet -> {
                try {
                    if (!resultSet.next()) {
                        return null;
                    }

                    return assemblyOrderFromResultSet(resultSet);
                } catch (SQLException e) {
                    throw new DataAccessException(String.format("Unable to find an order: \"%s\"", e.getMessage()), e);
                }
            });
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to find an order: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order insert(Connection connection, Order order) {
        String sql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";
        List<Object> parameters = List.of(order.getUsername().username(), order.isDone(), order.getUpdatedAt());

        try {
            long id = jdbcTemplate.update(connection, sql, parameters);
            return order.withId(OrderId.of(id));
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to insert an order: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connection connection, Order order) {
        String sql = "UPDATE ordering SET done = ?, updated_at = ? WHERE id = ?;";
        List<Object> parameters = List.of(order.isDone(), order.getUpdatedAt(), order.getId().id());

        try {
            jdbcTemplate.update(connection, sql, parameters);
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to update an order: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * Assemblies an {@link Order} from a {@link ResultSet}
     *
     * @param resultSet a result set with data
     *
     * @return an order
     * @throws SQLException if something was wrong
     */
    private Order assemblyOrderFromResultSet(ResultSet resultSet) throws SQLException {
        OrderId id = OrderId.of(resultSet.getLong("id"));
        OrderUsername username = OrderUsername.of(resultSet.getString("user_name"));
        boolean done = resultSet.getBoolean("done");
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");

        return Order.of(id, username, done, updatedAt);
    }
}
