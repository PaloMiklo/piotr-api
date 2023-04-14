package com.api.piotr.controller;

import static com.api.piotr.ObjectRandomizer.getRandomObject;
import static com.api.piotr.constant.ApiPaths.ORDER_LIST;
import static com.api.piotr.constant.ApiPaths.ORDER_PATH;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.service.OrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testGetAllOrders() throws Exception {
        List<OrderRowDto> ordersList = new ArrayList<>();
        ordersList.add(new OrderRowDto(1L));
        ordersList.add(new OrderRowDto(2L));
        Page<OrderRowDto> orders = new PageImpl<>(ordersList);
        given(orderService.getAllOrders(any(Pageable.class))).willReturn(orders);

        mockMvc.perform(get(ORDER_PATH + ORDER_LIST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)));

        verify(orderService, times(1)).getAllOrders(any(Pageable.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderDetDto order = getRandomObject(OrderDetDto.class);

        given(orderService.getOrderById(anyLong())).willReturn(order);

        mockMvc.perform(get(ORDER_PATH + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.deliveryOption").exists())
                .andExpect(jsonPath("$.billingOption").exists())
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.comment").exists())
                .andExpect(jsonPath("$.shippingAddress").exists())
                .andExpect(jsonPath("$.billingAddress").exists())
                .andExpect(jsonPath("$.cart.id").exists())
                .andExpect(jsonPath("$.cart.freeShipping").exists())
                .andExpect(jsonPath("$.cart.itemCount").exists())
                .andExpect(jsonPath("$.cart.cartPrice").exists())
                .andExpect(jsonPath("$.cart.lines").isArray());
    }
}
