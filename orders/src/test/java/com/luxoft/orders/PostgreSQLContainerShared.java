package com.luxoft.orders;

import com.luxoft.orders.persistent.migration.FlywayMigrationsExecutor;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgresqlContainerShared class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
public class PostgreSQLContainerShared extends PostgreSQLContainer<PostgreSQLContainerShared> {
    private static final String IMAGE_VERSION = "postgres:12.2";
    private static PostgreSQLContainerShared container;

    private PostgreSQLContainerShared() {
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainerShared getInstance() {
        if (container == null) {
            container = new PostgreSQLContainerShared();
        }

        return container;
    }

    @Override
    public void start() {
        super.start();
        FlywayMigrationsExecutor.execute(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }
}
