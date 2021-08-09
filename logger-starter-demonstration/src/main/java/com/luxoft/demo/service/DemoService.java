package com.luxoft.demo.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DemoService class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-09
 */
@Service
public class DemoService {
    private final Logger logger;

    @Autowired
    public DemoService(ObjectProvider<Logger> loggerObjectProvider) {
        this.logger = loggerObjectProvider.getObject(DemoService.class);
    }

    public void demo() {
        logger.info("Hello from service's logger");
    }
}
