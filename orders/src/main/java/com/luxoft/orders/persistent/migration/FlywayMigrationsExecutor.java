package com.luxoft.orders.persistent.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FlywayMigrationsExecutor class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-23
 */
public class FlywayMigrationsExecutor {
    private static final String MIGRATIONS_LOCATION = "classpath:/db/migration";
    private static final Logger logger = LoggerFactory.getLogger(FlywayMigrationsExecutor.class);

    /**
     * Executes migrations
     *
     * @param url      a database url
     * @param user     a database user
     * @param password a database user password
     *
     * @throws FlywayException if something was wrong
     */
    public static void execute(String url, String user, String password) throws FlywayException {
        logger.info("Start db migrations executing");

        var flyway = Flyway.configure()
            .dataSource(url, user, password)
            .locations(MIGRATIONS_LOCATION)
            .load();

        try {
            flyway.migrate();
        } catch (FlywayException e) {
            logger.error(String.format("Unable to execute migrations: \"%s\"", e.getMessage()));
            throw e;
        }

        logger.info("Db migrations were successfully finished");
    }
}
