package com.luxoft.orders.controller;

import com.google.gson.GsonBuilder;
import com.luxoft.orders.api.CreateOrderDto;
import com.luxoft.orders.api.CreateOrderItemDto;
import com.luxoft.orders.domain.OrderItemNotFoundException;
import com.luxoft.orders.domain.OrderNotFoundException;
import com.luxoft.orders.domain.OrderService;
import com.luxoft.orders.domain.model.Order;
import com.luxoft.orders.domain.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * OrderControllerTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-31
 */
@DisplayName("OrderControllerTest: Test for the REST-controller of orders")
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
            .build();
    }

    @DisplayName("Should return an order and 200 status if the order exists")
    @Test
    void shouldReturnOrderAnd200StatusIfOrderExists() throws Exception {
        // given
        var expectedOrder = Order.builder().build();

        // when
        given(orderService.getOrder(eq(1L)))
            .willReturn(expectedOrder);

        // then
        mockMvc.perform(get("/api/v1.0/orders/{orderId}", 1L).accept("application/json; charset=utf-8"))
            .andExpect(status().isOk());
    }

    @DisplayName("Should return 404 status if an order not found")
    @Test
    void shouldReturn404StatusIfOrderNotFound() throws Exception {
        // when
        given(orderService.getOrder(1L))
            .willThrow(OrderNotFoundException.class);

        // then
        mockMvc.perform(get("/api/v1.0/orders/{orderId}", 1L).accept("application/json; charset=utf-8"))
            .andExpect(status().isNotFound());
    }

    @DisplayName("Should create a new order and return 201 status if passed data is correct")
    @Test
    void shouldCreateNewOrderAndReturn201StatusIfPassedDataCorrect() throws Exception {
        // given
        var expectedOrder = Order.builder().build();
        var createOrderItemDtoShoes = new CreateOrderItemDto();

        createOrderItemDtoShoes.setName("Shoes");
        createOrderItemDtoShoes.setCount(15);
        createOrderItemDtoShoes.setPrice(BigDecimal.valueOf(1500L));

        var createOrderDto = new CreateOrderDto();

        createOrderDto.setUsername("Alex");
        createOrderDto.setItems(List.of(createOrderItemDtoShoes));

        var gson = new GsonBuilder().create();
        var createOrderDtoJson = gson.toJson(createOrderDto);

        // when
        given(orderService.createOrder(any(CreateOrderDto.class)))
            .willReturn(expectedOrder);

        // then
        mockMvc.perform(
                post("/api/v1.0/orders")
                    .accept("application/json; charset=utf-8")
                    .contentType("application/json")
                    .content(createOrderDtoJson)
            )
            .andExpect(status().isCreated());
    }

    @DisplayName("Should not create any order and return 400 status if passed data is incorrect")
    @Test
    void shouldNotCreateNewOrderAndReturn400StatusIfPassedDataIncorrect() throws Exception {
        // given
        var createOrderDto = new CreateOrderDto();
        createOrderDto.setUsername("Alex");

        var gson = new GsonBuilder().create();
        var createOrderDtoJson = gson.toJson(createOrderDto);

        // when / then
        mockMvc.perform(
            post("/api/v1.0/orders")
                .accept("application/json; charset=utf-8")
                .contentType("application/json")
                .content(createOrderDtoJson)
        )
            .andExpect(status().isBadRequest());

        verify(orderService, never())
            .createOrder(any(CreateOrderDto.class));
    }

    @DisplayName("Should add a new order item and return 201 status if order exists and passed data is correct")
    @Test
    void shouldAddOrderItemAndReturn201StatusIfOrderExistsAndPassedDataCorrect() throws Exception {
        // given
        var expectedOrderItem = OrderItem.builder().build();
        var createOrderItemDtoShoes = new CreateOrderItemDto();

        createOrderItemDtoShoes.setName("Shoes");
        createOrderItemDtoShoes.setCount(15);
        createOrderItemDtoShoes.setPrice(BigDecimal.valueOf(1500.50));

        var gson = new GsonBuilder().create();
        var createOrderItemDtoShoesJson = gson.toJson(createOrderItemDtoShoes);

        // when
        given(orderService.addOrderItem(eq(1L), any(CreateOrderItemDto.class)))
            .willReturn(expectedOrderItem);

        // then
        mockMvc.perform(
            post("/api/v1.0/orders/{orderId}/items", 1L)
                .accept("application/json; charset=utf-8")
                .contentType("application/json")
                .content(createOrderItemDtoShoesJson)
        )
            .andExpect(status().isCreated());
    }

    @DisplayName("Should not add any order item and return 400 status if passed data is incorrect")
    @Test
    void shouldNotAddOrderItemAndReturn400StatusIfPassedDataIncorrect() throws Exception {
        // given
        var createOrderItemDtoShoes = new CreateOrderItemDto();

        createOrderItemDtoShoes.setCount(15);
        createOrderItemDtoShoes.setPrice(BigDecimal.valueOf(1500L));

        var gson = new GsonBuilder().create();
        var createOrderItemDtoShoesJson = gson.toJson(createOrderItemDtoShoes);

        // when / then
        mockMvc.perform(
            post("/api/v1.0/orders/{orderId}/items", 1L)
                .accept("application/json; charset=utf-8")
                .contentType("application/json")
                .content(createOrderItemDtoShoesJson)
        )
            .andExpect(status().isBadRequest());

        verify(orderService, never())
            .addOrderItem(eq(1L), any(CreateOrderItemDto.class));
    }

    @DisplayName("Should not add any order item and return 404 status if passed data is correct but order not found")
    @Test
    void shouldNotAddOrderItemAndReturn404StatusIfOrderNotFound() throws Exception {
        // given
        var createOrderItemDtoShoes = new CreateOrderItemDto();

        createOrderItemDtoShoes.setName("Shoes");
        createOrderItemDtoShoes.setCount(15);
        createOrderItemDtoShoes.setPrice(BigDecimal.valueOf(1500L));

        var gson = new GsonBuilder().create();
        var createOrderItemDtoShoesJson = gson.toJson(createOrderItemDtoShoes);

        // when
        doThrow(OrderNotFoundException.class)
            .when(orderService)
            .addOrderItem(eq(1L), any(CreateOrderItemDto.class));

        // then
        mockMvc.perform(
            post("/api/v1.0/orders/{orderId}/items", 1L)
                .accept("application/json; charset=utf-8")
                .contentType("application/json")
                .content(createOrderItemDtoShoesJson)
        )
            .andExpect(status().isNotFound());
    }

    @DisplayName("Should change an order item count and return 204 status if item exists and passed data is correct")
    @Test
    void shouldChangeOrderItemCountAndReturn204StatusIfItemExistsAndPassedDataCorrect() throws Exception {
        // when / then
        mockMvc.perform(
            put("/api/v1.0/orders/items/{itemId}", 1L)
                .accept("application/json; charset=utf-8")
                .param("count", "35")
        )
            .andExpect(status().isNoContent());

        verify(orderService, times(1))
            .changeOrderItemCount(eq(1L), eq(35));
    }

    @DisplayName("Should not change any order item count and return 400 status if passed data is incorrect")
    @Test
    void shouldNotChangeOrderItemCountAndReturn400StatusIfPassedDataIncorrect() throws Exception {
        // when / then
        mockMvc.perform(
            put("/api/v1.0/orders/items/{itemId}", 1L)
                .accept("application/json; charset=utf-8")
                .param("count", "-40")
        )
            .andExpect(status().isBadRequest());

        verify(orderService, never())
            .changeOrderItemCount(eq(1L), anyInt());
    }

    @DisplayName("Should not change any order item count and return 404 status if the item not found")
    @Test
    void shouldNotChangeOrderItemCountAndReturn404StatusIfItemNotFound() throws Exception {
        // when
        doThrow(OrderItemNotFoundException.class)
            .when(orderService)
            .changeOrderItemCount(eq(1L), eq(35));

        // then
        mockMvc.perform(
            put("/api/v1.0/orders/items/{itemId}", 1L)
                .accept("application/json; charset=utf-8")
                .param("count", "35")
        )
            .andExpect(status().isNotFound());
    }

    @DisplayName("Should done all orders and return 204 status")
    @Test
    void shouldDoneAllOrdersAndReturn204Status() throws Exception {
        mockMvc.perform(put("/api/v1.0/orders/done"))
            .andExpect(status().isNoContent());
    }
}
