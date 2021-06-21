package com.luxoft.orders;

import com.luxoft.orders.domain.OrderNotFoundException;
import com.luxoft.orders.domain.OrderPositionExistsException;
import com.luxoft.orders.infrastructure.PostgresConnectionCreator;

import java.math.BigDecimal;
import java.sql.*;
import java.time.OffsetDateTime;

/**
 * OrdersApplication class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-15
 */
@Deprecated
public class OrdersApplication {
    private static final int UPDATE_BATCH_SIZE = 100;

    /**
     * The main application entrypoint
     *
     * This program expects that all passed data is valid (e.g. position name or count)
     * This program does not handle any exceptions
     * This program has not any "architecture" (only JDBC using example)
     * This program does not create any "custom" restrictions (only those which already were applied to tables)
     * This program follows for db structure that was given in a previous meeting
     *
     * @param args arguments
     *
     * @throws OrderPositionExistsException if an order position already exists
     * @throws OrderNotFoundException              if an order not found
     */
    public static void main(String[] args) throws OrderPositionExistsException, OrderNotFoundException {
        // createOrder("Armen");
        // addPositionToOrder(17L, "Куртка", 1, new BigDecimal("500"));
        // replacePositionCount(22L, 1);
        // showOrderDetails(17L);
        // doneOrders();
    }

    /**
     * Creates a new order
     *
     * @param username a name of user who creates an order
     */
    private static void createOrder(String username) {
        try (Connection connection = openConnection()) {
            String createOrderQueryStr = "INSERT INTO ordering (user_name, updated_at) VALUES (?, ?);";
            PreparedStatement createOrderStatement = connection.prepareStatement(createOrderQueryStr);

            createOrderStatement.setString(1, username);
            createOrderStatement.setObject(2, OffsetDateTime.now());

            createOrderStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new position to an order
     *
     * @param orderId       an id of order
     * @param positionName  a name of position
     * @param positionCount a count of position
     * @param positionPrice a price of position
     *
     * @throws OrderPositionExistsException if an order already has the same position
     */
    private static void addPositionToOrder(
        long orderId,
        String positionName,
        int positionCount,
        BigDecimal positionPrice
    ) throws OrderNotFoundException, OrderPositionExistsException {
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);

            // Check if order with passed id exists
            String selectOrderQueryStr = "SELECT id FROM ordering WHERE id = ? FOR UPDATE;";

            PreparedStatement selectOrderStatement = connection.prepareStatement(selectOrderQueryStr);
            selectOrderStatement.setLong(1, orderId);

            ResultSet orderResult = selectOrderStatement.executeQuery();
            if (!orderResult.isBeforeFirst()) {
                throw new OrderNotFoundException(String.format("Order %d not found", orderId));
            }

            // Check if the order has not such position
            String selectPositionQueryStr = "SELECT id FROM ordering_items WHERE ordering_id = ? AND item_name = ?;";
            PreparedStatement selectPositionStatement = connection.prepareStatement(selectPositionQueryStr);

            selectPositionStatement.setLong(1, orderId);
            selectPositionStatement.setString(2, positionName);

            ResultSet positionResult = selectPositionStatement.executeQuery();
            if (positionResult.isBeforeFirst()) {
                String messagePattern = "Order %d already has the \"%s\" position";
                throw new OrderPositionExistsException(String.format(messagePattern, orderId, positionName));
            }

            String addPositionQueryStr =
                "INSERT INTO ordering_items (ordering_id, item_name, item_count, item_price) " +
                "VALUES (?, ?, ?, ?);";

            PreparedStatement addPositionQueryStatement = connection.prepareStatement(addPositionQueryStr);

            addPositionQueryStatement.setLong(1, orderId);
            addPositionQueryStatement.setString(2, positionName);
            addPositionQueryStatement.setInt(3, positionCount);
            addPositionQueryStatement.setBigDecimal(4, positionPrice);

            addPositionQueryStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces position count for an order
     *
     * @param positionId an id of position
     * @param count      a new count of position
     */
    private static void replacePositionCount(long positionId, int count) {
        try (Connection connection = openConnection()) {
            String replacePositionCountQueryStr = "UPDATE ordering_items SET item_count = ? WHERE id = ?;";
            PreparedStatement replacePositionCountStatement = connection.prepareStatement(replacePositionCountQueryStr);

            replacePositionCountStatement.setInt(1, count);
            replacePositionCountStatement.setLong(2, positionId);

            replacePositionCountStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows order details (order id, username, positions, etc.)
     *
     * @param orderId an id of order
     */
    private static void showOrderDetails(long orderId) throws OrderNotFoundException {
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);

            String selectOrderQueryStr = "SELECT id, user_name FROM ordering WHERE id = ? FOR UPDATE;";

            PreparedStatement selectOrderStatement = connection.prepareStatement(selectOrderQueryStr);
            selectOrderStatement.setLong(1, orderId);

            ResultSet orderResult = selectOrderStatement.executeQuery();
            if (!orderResult.isBeforeFirst()) {
                throw new OrderNotFoundException(String.format("Order %d not found", orderId));
            }

            String selectOrderPositionsQueryStr =
                "SELECT item_name, item_count, item_price " +
                "FROM ordering_items " +
                "WHERE ordering_id = ?;";

            PreparedStatement selectOrderPositionsStatement = connection.prepareStatement(selectOrderPositionsQueryStr);
            selectOrderPositionsStatement.setLong(1, orderId);

            ResultSet orderPositionResult = selectOrderPositionsStatement.executeQuery();

            printDataFromResultSet(orderResult, 2);
            printDataFromResultSet(orderPositionResult, 3);

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dones all orders which are still non done
     *
     * This method blocks all table (share mode) for no one could not
     * create any additional order until other orders will not done
     */
    private static void doneOrders() {
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);

            String selectOrdersCountQueryStr = "SELECT COUNT(id) FROM ordering WHERE done = false;";
            PreparedStatement selectOrdersCountStatement = connection.prepareStatement(selectOrdersCountQueryStr);

            ResultSet countResult = selectOrdersCountStatement.executeQuery();
            countResult.next();

            long ordersCount = countResult.getLong(1);
            if (ordersCount == 0) {
                return;
            }

            long ordersDoneCount = 0;
            while (ordersDoneCount < ordersCount) {
                String doneOrdersQueryStr =
                    "WITH non_done_orders AS (" +
                        "SELECT id " +
                        "FROM ordering " +
                        "WHERE done = false " +
                            "FOR UPDATE " +
                                "SKIP LOCKED " +
                        "LIMIT ? " +
                    ")" +
                    "UPDATE ordering " +
                    "SET done = true " +
                    "FROM non_done_orders " +
                    "WHERE ordering.id = non_done_orders.id;";

                PreparedStatement doneOrdersStatement = connection.prepareStatement(doneOrdersQueryStr);
                doneOrdersStatement.setInt(1, UPDATE_BATCH_SIZE);

                doneOrdersStatement.executeUpdate();
                ordersDoneCount += UPDATE_BATCH_SIZE;
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a new {@link Connection}
     *
     * @return a connection (localed psql in fact)
     * @throws SQLException if something was wrong
     */
    private static Connection openConnection() throws SQLException {
        return PostgresConnectionCreator.create();
    }

    /**
     * Prints all data from a {@link ResultSet}
     *
     * @param resultSet     a result set
     * @param columnsNumber a columns number
     *
     * @throws SQLException if something was wrong
     */
    private static void printDataFromResultSet(ResultSet resultSet, int columnsNumber) throws SQLException {
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) {
                    System.out.print(",  ");
                }

                String columnValue = resultSet.getString(i);
                System.out.print(columnValue);
            }

            System.out.println();
        }
    }
}
