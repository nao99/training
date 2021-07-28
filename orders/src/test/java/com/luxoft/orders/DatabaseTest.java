package com.luxoft.orders;

import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import com.luxoft.orders.util.HibernateUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * DatabaseTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-19
 */
public abstract class DatabaseTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER =
        PostgreSQLContainerShared.getInstance();

    static {
        POSTGRESQL_CONTAINER.start();
    }

    protected static SessionFactory sessionFactory;

    @BeforeAll
    public static void init() {
        var configuration = new Configuration();

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", POSTGRESQL_CONTAINER.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", POSTGRESQL_CONTAINER.getUsername());
        configuration.setProperty("hibernate.connection.password", POSTGRESQL_CONTAINER.getPassword());
        configuration.setProperty("hibernate.connection.pool_size", "10");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.generate_statistics", "true");

        sessionFactory = HibernateUtils.buildSessionFactory(configuration, Order.class, OrderItem.class);
    }
}
