package com.luxoft.orders;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.persistent.api.JdbcOrderItemRepository;
import com.luxoft.orders.persistent.api.JdbcOrderRepository;
import com.luxoft.orders.domain.OrderServiceImpl;
import com.luxoft.orders.persistent.migration.FlywayMigrationsExecutor;
import com.luxoft.orders.persistent.query.JdbcTemplateImpl;
import com.luxoft.orders.persistent.transaction.JdbcTransactionRunner;
import com.zaxxer.hikari.HikariDataSource;

import java.math.BigDecimal;
import java.util.List;

/**
 * DemonstrationApplication class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-24
 */
public class DemonstrationApplication {
    private static final String DB_URL = "jdbc:postgresql://172.50.1.2/app";
    private static final String DB_USERNAME = "dbuser";
    private static final String DB_PASSWORD = "dbuser";

    public static void main(String[] args) {
        // 0. Preparation
        var dataSource = new HikariDataSource();

        dataSource.setAutoCommit(false);
        dataSource.setJdbcUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

        FlywayMigrationsExecutor.execute(DB_URL, DB_USERNAME, DB_PASSWORD);

        var jdbcTemplate = new JdbcTemplateImpl();
        var jdbcTransactionRunner = new JdbcTransactionRunner(dataSource);

        var jdbcOrderItemRepository = new JdbcOrderItemRepository(jdbcTemplate);
        var jdbcOrderRepository = new JdbcOrderRepository(jdbcTemplate, jdbcOrderItemRepository);

        var orderService = new OrderServiceImpl(jdbcTransactionRunner, jdbcOrderRepository, jdbcOrderItemRepository);

        // 1. Create order
        var createOrderItemDtoShoes = new CreateOrderItemDto();

        createOrderItemDtoShoes.setName("Shoes");
        createOrderItemDtoShoes.setCount(15);
        createOrderItemDtoShoes.setPrice(BigDecimal.valueOf(1500L));

        var createOrderDto = new CreateOrderDto();

        createOrderDto.setUsername("Alex");
        createOrderDto.setItems(List.of(createOrderItemDtoShoes));

        var createdOrder = orderService.createOrder(createOrderDto);

        // 2.Get order
        var selectedOrder = orderService.getOrder(createdOrder.getId());

        // 2.1 Print order
        System.out.println(selectedOrder);

        // 3. Add order item
        var createOrderItemDtoGloves = new CreateOrderItemDto();

        createOrderItemDtoGloves.setName("Gloves");
        createOrderItemDtoGloves.setCount(30);
        createOrderItemDtoGloves.setPrice(BigDecimal.valueOf(100L));

        orderService.addOrderItem(selectedOrder.getId(), createOrderItemDtoGloves);

        // 4.Change order item count
        orderService.changeOrderItemCount(selectedOrder.getItems().get(0).getId(), 10);

        // 5. Done all orders
        orderService.doneAllOrders();
    }
}
