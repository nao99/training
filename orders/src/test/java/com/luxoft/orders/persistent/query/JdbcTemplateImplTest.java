package com.luxoft.orders.persistent.query;

import com.luxoft.orders.PostgreSQLContainerShared;
import com.luxoft.orders.persistent.DatabaseException;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JdbcTemplateImplTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class JdbcTemplateImplTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER =
        PostgreSQLContainerShared.getInstance();

    static {
        POSTGRESQL_CONTAINER.start();
    }

    private DataSource dataSource;
    private JdbcTemplateImpl template;

    @BeforeEach
    public void setUp() throws Exception {
        var hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        hikariDataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        hikariDataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        hikariDataSource.setMaximumPoolSize(2);

        dataSource = hikariDataSource;
        template = new JdbcTemplateImpl();
    }

    @Test
    public void updateWhenRecordExists() throws Exception {
        // given
        List<Object> parameters = List.of("Alex", false, LocalDateTime.now());

        var createOrderSql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";
        var selectOrderDoneSql = "SELECT done FROM ordering WHERE id = ?;";
        var updateOrderDoneSql = "UPDATE ordering SET done = true WHERE id = ?;";

        Function<ResultSet, Boolean> handler = rs -> {
            try {
                rs.next();
                return rs.getBoolean("done");
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        };

        try (var connection = dataSource.getConnection()) {
            var createdOrderId = template.update(connection, createOrderSql, parameters);
            List<Object> selectionParameters = List.of(createdOrderId);

            var selectedOrderDone = template.select(connection, selectOrderDoneSql, selectionParameters, handler);

            assertTrue(selectedOrderDone.isPresent());
            assertFalse(selectedOrderDone.get());

            // when
            template.update(connection, updateOrderDoneSql, List.of(createdOrderId));
            var selectedOrderDoneUpdated = template.select(connection, selectOrderDoneSql, selectionParameters, handler);

            // then
            assertTrue(selectedOrderDoneUpdated.isPresent());
            assertTrue(selectedOrderDoneUpdated.get());
        }
    }

    @Test
    public void updateWhenRecordNotExists() throws Exception {
        // given
        List<Object> parameters = List.of("Alex", false, LocalDateTime.now());
        var sql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";

        try (var connection = dataSource.getConnection()) {
            // when
            var result = template.update(connection, sql, parameters);

            // then
            assertTrue(result > 0);
        }
    }

    @Test
    public void updateWhenUnableToCreatePreparedStatement() throws Exception {
        // given
        List<Object> parameters = List.of("Alex", false, LocalDateTime.now());
        var sql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";

        try (var connection = dataSource.getConnection()) {
            connection.close();

            // when / then
            assertThrows(DatabaseException.class, () -> template.update(connection, sql, parameters));
        }
    }

    @Test
    public void updateWhenUnableToPrepareParametersToStatement() throws Exception {
        // given
        List<Object> parameters = List.of("Alex", false, new UnknownParameterType());
        var sql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";

        try (var connection = dataSource.getConnection()) {
            // when / then
            assertThrows(DatabaseException.class, () -> template.update(connection, sql, parameters));
        }
    }

    @Test
    public void updateWhenUnableToExecuteUpdateStatement() throws Exception {
        // given
        List<Object> parameters = List.of("Alex", false);
        var sql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";

        try (var connection = dataSource.getConnection()) {
            connection.close();

            // when / then
            assertThrows(DatabaseException.class, () -> template.update(connection, sql, parameters));
        }
    }

    @Test
    public void select() throws Exception {
        // given
        List<Object> createOrderParameters = List.of("Alex", false, LocalDateTime.now());

        var createOrderSql = "INSERT INTO ordering (user_name, done, updated_at) VALUES (?, ?, ?);";
        var selectOrderUsernameSql = "SELECT user_name FROM ordering WHERE id = ?;";

        try (var connection = dataSource.getConnection()) {
            var orderId = template.update(connection, createOrderSql, createOrderParameters);

            // when
            var result = template.select(connection, selectOrderUsernameSql, List.of(orderId), rs -> {
                try {
                    rs.next();
                    return rs.getString("user_name");
                } catch (SQLException e) {
                    throw new RuntimeException();
                }
            });

            // then
            assertTrue(result.isPresent());
            assertEquals(createOrderParameters.get(0), result.get());
        }
    }

    @Test
    public void selectWhenUnableToCreatePreparedStatement() throws Exception {
        // given
        try (var connection = dataSource.getConnection()) {
            connection.close();

            // when / then
            assertThrows(
                DatabaseException.class,
                () -> template.select(connection, "SELECT id FROM ordering;", List.of(), rs -> null)
            );
        }
    }

    @Test
    public void selectWhenUnableToPrepareParametersToStatement() throws Exception {
        // given
        List<Object> parameters = List.of(new UnknownParameterType());
        try (var connection = dataSource.getConnection()) {
            // when / then
            assertThrows(
                DatabaseException.class,
                () -> template.select(connection, "SELECT id FROM ordering WHERE id = ?;", parameters, rs -> null)
            );
        }
    }

    @Test
    public void selectWhenUnableToExecuteQuery() throws Exception {
        // given
        var sql = "SELECT user_name FROM ordering WHERE id = ?;";
        try (var connection = dataSource.getConnection()) {
            // when / then
            assertThrows(DatabaseException.class, () -> template.select(connection, sql, List.of(), rs -> null));
        }
    }

    private static class UnknownParameterType {
    }
}
