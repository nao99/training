package com.luxoft.orders;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * DatabaseTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-02
 */
@SpringBootTest
@ContextConfiguration(initializers = {DatabaseTest.Initializer.class})
public abstract class DatabaseTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRESQL_CONTAINER =
        PostgreSQLContainerShared.getInstance();

    static {
        POSTGRESQL_CONTAINER.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            var testPropertyValues = TestPropertyValues.of(
                "spring.datasource.url=" + POSTGRESQL_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRESQL_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRESQL_CONTAINER.getPassword()
            );

            testPropertyValues.applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
