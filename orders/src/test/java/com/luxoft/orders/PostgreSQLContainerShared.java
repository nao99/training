package com.luxoft.orders;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgresqlContainerShared class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
public class PostgreSQLContainerShared extends PostgreSQLContainer<PostgreSQLContainerShared> {
    private static final String IMAGE = "postgres:12.2";
    private static PostgreSQLContainerShared container;

    private PostgreSQLContainerShared() {
        super(IMAGE);
    }

    public static PostgreSQLContainerShared getInstance() {
        if (container == null) {
            container = new PostgreSQLContainerShared();
        }

        return container;
    }
}
