package com.luxoft.orders.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * PostgresConnectionCreator class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-16
 */
@Deprecated
public class PostgresConnectionCreator {
    /**
     * Creates a PostgreSQL connection
     *
     * @return a psql connection
     */
    public static Connection create() throws SQLException {
        String url = "jdbc:postgresql://172.50.1.2/app";

        Properties props = new Properties();
        props.setProperty("user", "dbuser");
        props.setProperty("password", "dbuser");

        return DriverManager.getConnection(url, props);
    }
}
