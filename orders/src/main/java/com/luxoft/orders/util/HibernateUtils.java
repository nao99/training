package com.luxoft.orders.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

/**
 * HibernateUtils class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-19
 */
public class HibernateUtils {
    /**
     * Builds a {@link SessionFactory}
     *
     * @param configResourceFileName a name of file with Hibernate settings
     * @param annotatedClasses       classes which contain annotations
     *
     * @return a session factory
     */
    public static SessionFactory buildSessionFactory(String configResourceFileName, Class<?>... annotatedClasses) {
        var configuration = new Configuration();
        configuration.configure(configResourceFileName);

        var serviceRegistry = createServiceRegistry(configuration);

        var metadataSources = new MetadataSources(serviceRegistry);
        Arrays.stream(annotatedClasses)
            .forEach(metadataSources::addAnnotatedClass);

        var metadata = metadataSources.getMetadataBuilder()
            .build();

        return metadata.getSessionFactoryBuilder()
            .build();
    }

    /**
     * Builds a {@link SessionFactory}
     *
     * @param configuration    a configuration
     * @param annotatedClasses classes which contain annotations
     *
     * @return a session factory
     */
    public static SessionFactory buildSessionFactory(Configuration configuration, Class<?>... annotatedClasses) {
        var serviceRegistry = createServiceRegistry(configuration);

        var metadataSources = new MetadataSources(serviceRegistry);
        Arrays.stream(annotatedClasses)
            .forEach(metadataSources::addAnnotatedClass);

        var metadata = metadataSources.getMetadataBuilder()
            .build();

        return metadata.getSessionFactoryBuilder()
            .build();
    }

    private static StandardServiceRegistry createServiceRegistry(Configuration configuration) {
        var serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        return serviceRegistryBuilder.applySettings(configuration.getProperties())
            .build();
    }
}
