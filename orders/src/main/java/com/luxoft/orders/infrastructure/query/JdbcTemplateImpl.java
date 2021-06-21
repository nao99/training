package com.luxoft.orders.infrastructure.query;

import com.luxoft.orders.infrastructure.DatabaseException;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * JdbcTemplateImpl class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-18
 */
public class JdbcTemplateImpl implements JdbcTemplate {
    /**
     * {@inheritDoc}
     */
    @Override
    public long update(Connection connection, String sql, List<Object> parameters) throws DatabaseException {
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareParametersToStatement(statement, parameters);
            statement.executeUpdate();

            try (var resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Unable to execute update query: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> select(
        Connection connection,
        String sql,
        List<Object> parameters,
        Function<ResultSet, T> handler
    ) throws DatabaseException {
        try (var statement = connection.prepareStatement(sql)) {
            prepareParametersToStatement(statement, parameters);
            try (var resultSet = statement.executeQuery()) {
                return Optional.ofNullable(handler.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Unable to execute select query: \"%s\"", e.getMessage()), e);
        }
    }

    /**
     * Prepares all passed parameters to a {@link PreparedStatement}
     *
     * @param statement  a statement
     * @param parameters a parameters list
     */
    private void prepareParametersToStatement(
        PreparedStatement statement,
        List<Object> parameters
    ) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }
    }
}
