package com.luxoft.demo.controller;

import com.luxoft.demo.service.DemoService;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-09
 */
@RestController
public class DemoController {
    private final Logger logger;
    private final DemoService service;

    @Autowired
    public DemoController(ObjectProvider<Logger> loggerObjectProvider, DemoService service) {
        this.logger = loggerObjectProvider.getObject(DemoController.class);
        this.service = service;
    }

    @GetMapping("/demo")
    public void demo() {
        logger.info("Hello from controller's logger");
        service.demo();
    }
}
