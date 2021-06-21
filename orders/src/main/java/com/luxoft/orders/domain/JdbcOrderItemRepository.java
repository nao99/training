package com.luxoft.orders.domain;

import com.luxoft.orders.domain.model.*;
import com.luxoft.orders.infrastructure.DataAccessException;
import com.luxoft.orders.infrastructure.DatabaseException;
import com.luxoft.orders.infrastructure.query.JdbcTemplate;

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
 * @since   2021-06-21
 */
public class JdbcOrderItemRepository implements OrderItemRepository {
    /**
     * JDBC template
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * JdbcOrderItemRepository constructor
     *
     * @param jdbcTemplate a jdbc template
     */
    public JdbcOrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OrderItem> find(Connection connection, OrderItemId orderItemId) throws DataAccessException {
        String sql = "SELECT id, ordering_id, item_name, item_count, item_price FROM ordering_items WHERE id = ?;";
        try {
            return jdbcTemplate.select(connection, sql, List.of(orderItemId.id()), resultSet -> {
                try {
                    if (!resultSet.next()) {
                        return null;
                    }

                    return assemblyOrderItemFromResultSet(resultSet);
                } catch (SQLException e) {
                    String errorMessage = String.format("Unable to find an order item: \"%s\"", e.getMessage());
                    throw new DataAccessException(errorMessage, e);
                }
            });
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to find an order item: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OrderItem> findByOrderIdAndName(
        Connection connection,
        OrderId orderId,
        OrderItemName name
    ) throws DataAccessException {
        String sql = "" +
            "SELECT id, ordering_id, item_name, item_count, item_price " +
            "FROM ordering_items " +
            "WHERE ordering_id = ? AND item_name = ?;";

        try {
            return jdbcTemplate.select(connection, sql, List.of(orderId.id(), name.name()), resultSet -> {
                try {
                    if (!resultSet.next()) {
                        return null;
                    }

                    return assemblyOrderItemFromResultSet(resultSet);
                } catch (SQLException e) {
                    String errorMessage = String.format("Unable to find an order item: \"%s\"", e.getMessage());
                    throw new DataAccessException(errorMessage, e);
                }
            });
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to find an order item: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<OrderItem> findByOrderId(Connection connection, OrderId orderId) throws DataAccessException {
        String sql = "SELECT id, ordering_id, item_name, item_count, item_price FROM ordering_items WHERE ordering_id = ?;";
        return jdbcTemplate.select(connection, sql, List.of(orderId.id()), resultSet -> {
            try {
                List<OrderItem> itemsList = new ArrayList<>();
                while (resultSet.next()) {
                    OrderItem item = assemblyOrderItemFromResultSet(resultSet);
                    itemsList.add(item);
                }

                return itemsList;
            } catch (SQLException e) {
                String errorMessage = String.format("Unable to find order items: \"%s\"", e.getMessage());
                throw new DataAccessException(errorMessage, e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderItem insert(Connection connection, OrderItem orderItem) throws DataAccessException {
        String sql = "INSERT INTO ordering_items (ordering_id, item_name, item_count, item_price) VALUES (?, ?, ?, ?);";
        List<Object> parameters = List.of(
            orderItem.getOrderId().id(),
            orderItem.getName().name(),
            orderItem.getCount().count(),
            orderItem.getPrice().price()
        );

        try {
            long id = jdbcTemplate.update(connection, sql, parameters);
            return orderItem.withId(OrderItemId.of(id));
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to insert an order item: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connection connection, OrderItem orderItem) throws DataAccessException {
        String sql = "UPDATE ordering_items SET item_count = ?, item_price = ? WHERE id = ?;";
        List<Object> parameters = List.of(
            orderItem.getCount().count(),
            orderItem.getPrice().price(),
            orderItem.getId().id()
        );

        try {
            jdbcTemplate.update(connection, sql, parameters);
        } catch (DatabaseException e) {
            throw new DataAccessException(String.format("Unable to insert an order item: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * Assemblies an {@link OrderItem} from a {@link ResultSet}
     *
     * @param resultSet a result set with data
     *
     * @return an order item
     * @throws SQLException if something was wrong
     */
    private OrderItem assemblyOrderItemFromResultSet(ResultSet resultSet) throws SQLException {
        OrderItemId id = OrderItemId.of(resultSet.getLong("id"));
        OrderId orderIdFound = OrderId.of(resultSet.getLong("ordering_id"));
        OrderItemName name = OrderItemName.of(resultSet.getString("item_name"));
        OrderItemCount count = OrderItemCount.of(resultSet.getInt("item_count"));
        OrderItemPrice price = OrderItemPrice.of(resultSet.getBigDecimal("item_price"));

        return OrderItem.of(id, orderIdFound, name, count, price);
    }
}
