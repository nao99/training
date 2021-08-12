package com.luxoft.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LoggerAutoConfiguration class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-09
 */
@Configuration
@ConditionalOnClass(ch.qos.logback.classic.Logger.class)
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public Logger logger() {
        return logger;
    }
}
