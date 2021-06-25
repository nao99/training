package com.luxoft.orders.persistent.query;

import com.luxoft.orders.PostgreSQLContainerShared;
import com.luxoft.orders.persistent.DatabaseException;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void updateWhenRecordStillNotExists() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        var preparedStatementMock = mock(PreparedStatement.class);
        var resultSetMock = mock(ResultSet.class);

        long expectedReturnValue = 15L;

        // when
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenReturn(resultSetMock);

        when(resultSetMock.getLong(1))
            .thenReturn(expectedReturnValue);

        long result = template.update(connectionMock, sql, parameters);

        // then
        verify(connectionMock, times(1))
            .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        verify(preparedStatementMock, times(1))
            .executeUpdate();

        verify(preparedStatementMock, times(1))
            .setObject(1, parameters.get(0));

        verify(resultSetMock, times(1))
            .next();

        verify(resultSetMock, times(1))
            .getLong(1);

        assertEquals(expectedReturnValue, result);
    }

    @Test
    public void updateWhenUnableToCreatePreparedStatement() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    @Test
    public void updateWhenUnableToPrepareParametersToStatement() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        var preparedStatementMock = mock(PreparedStatement.class);
        var sqlExceptionMock = mock(SQLException.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        doThrow(sqlExceptionMock)
            .when(preparedStatementMock)
            .setObject(1, parameters.get(0));

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    @Test
    public void updateWhenUnableToExecuteUpdateStatement() throws Exception {
        var expectedValue = "Alex";
        try (var connection = dataSource.getConnection()) {
            // when / then
            assertThrows(DatabaseException.class, () -> template.update(
                connection,
                "INSERT INTO jdbc_template_test (id, text) VALUES (?, ?);",
                List.of(expectedValue)
            ));
        }
    }

    @Test
    public void updateWhenUnableToGetGeneratedKeys() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        var preparedStatementMock = mock(PreparedStatement.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    @Test
    public void updateWhenUnableToGetNextReturnedValue() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        var preparedStatementMock = mock(PreparedStatement.class);
        var resultSetMock = mock(ResultSet.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenReturn(resultSetMock);

        when(resultSetMock.next())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    @Test
    public void updateWhenUnableToGetNextReturnedValueAsLong() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        var preparedStatementMock = mock(PreparedStatement.class);
        var resultSetMock = mock(ResultSet.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenReturn(resultSetMock);

        when(resultSetMock.getLong(1))
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    @Test
    public void select() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Optional<String> expectedReturnValue = Optional.of("Alex");

        // when
        when(connectionMock.prepareStatement(sql))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.executeQuery())
            .thenReturn(resultSetMock);

        Optional<String> result = template.select(connectionMock, sql, parameters, handler);

        // then
        verify(connectionMock, times(1))
            .prepareStatement(sql);

        verify(preparedStatementMock, times(1))
            .setObject(1, parameters.get(0));

        verify(preparedStatementMock, times(1))
            .executeQuery();

        assertEquals(expectedReturnValue, result);
    }

    @Test
    public void selectWhenUnableToCreatePreparedStatement() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        // when / then
        when(connectionMock.prepareStatement(sql))
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.select(connectionMock, sql, parameters, handler));
    }

    @Test
    public void selectWhenUnableToPrepareParametersToStatement() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        var preparedStatementMock = mock(PreparedStatement.class);
        var sqlExceptionMock = mock(SQLException.class);

        // when / then
        when(connectionMock.prepareStatement(sql))
            .thenReturn(preparedStatementMock);

        doThrow(sqlExceptionMock)
            .when(preparedStatementMock)
            .setObject(1, parameters.get(0));

        assertThrows(DatabaseException.class, () -> template.select(connectionMock, sql, parameters, handler));
    }

    @Test
    public void selectWhenUnableToExecuteQuery() throws Exception {
        // given
        var connectionMock = mock(Connection.class);
        var sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        var preparedStatementMock = mock(PreparedStatement.class);

        // when / then
        when(connectionMock.prepareStatement(sql))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.executeQuery())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.select(connectionMock, sql, parameters, handler));
    }
}
