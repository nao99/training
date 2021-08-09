package com.luxoft.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * LoggerAutoConfiguration class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-09
 */
@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {
    private final LoggerProperties properties;

    @Autowired
    public LoggerAutoConfiguration(LoggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
