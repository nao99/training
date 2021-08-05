package com.luxoft.orders.controller;

import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.api.OrderDto;
import com.luxoft.orders.api.OrderItemDto;
import com.luxoft.orders.domain.OrderItemNotFoundException;
import com.luxoft.orders.domain.OrderNotFoundException;
import com.luxoft.orders.domain.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * OrderController class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-31
 */
@RestController
@RequestMapping(path = "/api/v1.0/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/{orderId:\\d+}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") Long id) {
        try {
            var order = orderService.getOrder(id);
            var orderDto = OrderDto.of(order);

            return ResponseEntity.ok(orderDto);
        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
        var order = orderService.createOrder(createOrderDto);
        var orderDto = OrderDto.of(order);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderDto);
    }

    @PostMapping(path = "/{orderId:\\d+}/items")
    public ResponseEntity<OrderItemDto> addOrderItem(
        @PathVariable("orderId") Long orderId,
        @Valid @RequestBody CreateOrderItemDto createOrderItemDto
    ) {
        try {
            var orderItem = orderService.addOrderItem(orderId, createOrderItemDto);
            var orderItemDto = OrderItemDto.of(orderItem);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderItemDto);
        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping(path = "/items/{itemId:\\d+}")
    public ResponseEntity<Void> changeOrderItemCount(
        @PathVariable("itemId") Long itemId,
        @RequestParam("count") int count
    ) {
        if (count <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Count should be greater than 0");
        }

        try {
            orderService.changeOrderItemCount(itemId, count);
            return ResponseEntity.noContent()
                .build();
        } catch (OrderItemNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping(path = "/done")
    public ResponseEntity<Void> doneAllOrders() {
        orderService.doneAllOrders();
        return ResponseEntity.noContent()
            .build();
    }
}
