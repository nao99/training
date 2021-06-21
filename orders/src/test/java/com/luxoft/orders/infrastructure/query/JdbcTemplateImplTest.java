package com.luxoft.orders.infrastructure.query;

import com.luxoft.orders.infrastructure.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    /**
     * JDBC template
     */
    private JdbcTemplateImpl template;

    /**
     * Sets up
     */
    @BeforeEach
    public void setUp() {
        template = new JdbcTemplateImpl();
    }

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void update() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

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

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void updateWhenUnableToCreatePreparedStatement() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void updateWhenUnableToPrepareParametersToStatement() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        SQLException sqlExceptionMock = mock(SQLException.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        doThrow(sqlExceptionMock)
            .when(preparedStatementMock)
            .setObject(1, parameters.get(0));

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void updateWhenUnableToExecuteUpdateStatement() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.executeUpdate())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void updateWhenUnableToGetGeneratedKeys() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void updateWhenUnableToGetNextReturnedValue() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenReturn(resultSetMock);

        when(resultSetMock.next())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    /**
     * Test for {@link JdbcTemplateImpl#update(Connection, String, List)}
     */
    @Test
    public void updateWhenUnableToGetNextReturnedValueAsLong() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "UPDATE ordering SET done = true WHERE id = ?;";
        List<Object> parameters = List.of(15);

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        // when / then
        when(connectionMock.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.getGeneratedKeys())
            .thenReturn(resultSetMock);

        when(resultSetMock.getLong(1))
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.update(connectionMock, sql, parameters));
    }

    /**
     * Test for {@link JdbcTemplateImpl#select(Connection, String, List, Function)}
     */
    @Test
    public void select() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "SELECT user_name FROM ordering WHERE id = ?;";
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

    /**
     * Test for {@link JdbcTemplateImpl#select(Connection, String, List, Function)}
     */
    @Test
    public void selectWhenUnableToCreatePreparedStatement() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        // when / then
        when(connectionMock.prepareStatement(sql))
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.select(connectionMock, sql, parameters, handler));
    }

    /**
     * Test for {@link JdbcTemplateImpl#select(Connection, String, List, Function)}
     */
    @Test
    public void selectWhenUnableToPrepareParametersToStatement() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        SQLException sqlExceptionMock = mock(SQLException.class);

        // when / then
        when(connectionMock.prepareStatement(sql))
            .thenReturn(preparedStatementMock);

        doThrow(sqlExceptionMock)
            .when(preparedStatementMock)
            .setObject(1, parameters.get(0));

        assertThrows(DatabaseException.class, () -> template.select(connectionMock, sql, parameters, handler));
    }

    /**
     * Test for {@link JdbcTemplateImpl#select(Connection, String, List, Function)}
     */
    @Test
    public void selectWhenUnableToExecuteQuery() throws Exception {
        // given
        Connection connectionMock = mock(Connection.class);
        String sql = "SELECT user_name FROM ordering WHERE id = ?;";
        List<Object> parameters = List.of(15);
        Function<ResultSet, String> handler = (resultSet) -> "Alex";

        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);

        // when / then
        when(connectionMock.prepareStatement(sql))
            .thenReturn(preparedStatementMock);

        when(preparedStatementMock.executeQuery())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> template.select(connectionMock, sql, parameters, handler));
    }
}
