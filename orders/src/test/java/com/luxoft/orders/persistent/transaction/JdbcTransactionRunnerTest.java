package com.luxoft.orders.persistent.transaction;

import com.luxoft.orders.persistent.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JdbcTransactionRunnerTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-21
 */
class JdbcTransactionRunnerTest {
    /**
     * Data source mock
     */
    private DataSource dataSourceMock;

    /**
     * JDBC transaction runner
     */
    private JdbcTransactionRunner transactionRunner;

    /**
     * Sets up
     */
    @BeforeEach
    public void setUp() {
        dataSourceMock = mock(DataSource.class);
        transactionRunner = new JdbcTransactionRunner(dataSourceMock);
    }

    /**
     * Test for {@link JdbcTransactionRunner#run(TransactionOperation)}
     */
    @Test
    public void run() throws Exception {
        // given
        String expectedReturnResult = "Alex";
        TransactionOperation<String> operation = (connection) -> expectedReturnResult;

        Connection connectionMock = mock(Connection.class);

        // when
        when(dataSourceMock.getConnection())
            .thenReturn(connectionMock);

        String result = transactionRunner.run(operation);

        // then
        verify(dataSourceMock, times(1))
            .getConnection();

        verify(connectionMock, times(1))
            .commit();

        verify(connectionMock, never())
            .rollback();

        assertEquals(expectedReturnResult, result);
    }

    /**
     * Test for {@link JdbcTransactionRunner#run(TransactionOperation)}
     */
    @Test
    public void runWhenUnableToEstablishConnection() throws Exception {
        // given
        String expectedReturnResult = "Alex";
        TransactionOperation<String> operation = (connection) -> expectedReturnResult;

        Connection connectionMock = mock(Connection.class);

        // when / then
        when(dataSourceMock.getConnection())
            .thenThrow(SQLException.class);

        assertThrows(DatabaseException.class, () -> transactionRunner.run(operation));
    }

    /**
     * Test for {@link JdbcTransactionRunner#run(TransactionOperation)}
     */
    @Test
    public void runWheUnableToCommit() throws Exception {
        // given
        String expectedReturnResult = "Alex";
        TransactionOperation<String> operation = (connection) -> expectedReturnResult;

        Connection connectionMock = mock(Connection.class);
        SQLException sqlExceptionMock = mock(SQLException.class);

        // when / then
        when(dataSourceMock.getConnection())
            .thenReturn(connectionMock);

        doThrow(sqlExceptionMock)
            .when(connectionMock)
            .commit();

        assertThrows(DatabaseException.class, () -> transactionRunner.run(operation));

        verify(connectionMock, times(1))
            .commit();

        verify(connectionMock, times(1))
            .rollback();
    }

    /**
     * Test for {@link JdbcTransactionRunner#run(TransactionOperation)}
     */
    @Test
    public void runWhenUnableToRollback() throws Exception {
        // given
        String expectedReturnResult = "Alex";
        TransactionOperation<String> operation = (connection) -> expectedReturnResult;

        Connection connectionMock = mock(Connection.class);
        SQLException sqlExceptionMock = mock(SQLException.class);

        // when / then
        when(dataSourceMock.getConnection())
            .thenReturn(connectionMock);

        doThrow(sqlExceptionMock)
            .when(connectionMock)
            .commit();

        doThrow(sqlExceptionMock)
            .when(connectionMock)
            .rollback();

        assertThrows(DatabaseException.class, () -> transactionRunner.run(operation));

        verify(connectionMock, times(1))
            .commit();

        verify(connectionMock, times(1))
            .rollback();
    }
}
