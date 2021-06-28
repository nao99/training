package com.luxoft.orders.persistent.query;

import com.luxoft.orders.persistent.DatabaseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * JdbcTemplate interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public interface JdbcTemplate {
    /**
     * Executes an SQL query such as "UPDATE", "INSERT" or "DELETE"
     *
     * @param connection an established connection to db
     * @param sql        an sql query
     * @param parameters a list of parameters
     *
     * @return a first generated key
     * @throws DatabaseException if something was wrong
     */
    long update(Connection connection, String sql, List<Object> parameters) throws DatabaseException;

    /**
     * Executes an SQL "SELECT" query
     *
     * @param connection an established connection to db
     * @param sql        an sql query
     * @param parameters a list of parameters
     * @param handler    a handler that will triggers after successfully query execution
     *
     * @return an optional result
     * @throws DatabaseException if something was wrong
     */
    <T> Optional<T> select(
        Connection connection,
        String sql,
        List<Object> parameters,
        Function<ResultSet, T> handler
    ) throws DatabaseException;
}