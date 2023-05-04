package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.ORDER_CREATE;
import static com.api.piotr.constant.ApiPaths.ORDER_LIST;
import static com.api.piotr.constant.ApiPaths.ORDER_PATH;
import static com.api.piotr.util.ObjectRandomizer.generateRandomObject;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderNewDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void getAllOrders() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            List<OrderRowDto> ordersList = new ArrayList<>();
            ordersList.add(generateRandomObject(OrderRowDto.class));
            ordersList.add(generateRandomObject(OrderRowDto.class));
            Long id1 = ordersList.get(0).id();
            Long id2 = ordersList.get(1).id();

            given(orderService.getAllOrders(any(Pageable.class))).willReturn(new PageImpl<>(ordersList));

            mockMvc.perform(get(ORDER_PATH + ORDER_LIST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].id", is(id1)))
                    .andExpect(jsonPath("$.content[1].id", is(id2)));

            verify(orderService, times(1)).getAllOrders(any(Pageable.class));
            verifyNoMoreInteractions(orderService);
        });
    }

    @Test
    public void getOrderById() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            given(orderService.getOrderById(anyLong())).willReturn(generateRandomObject(OrderDetDto.class));

            mockMvc.perform(get(ORDER_PATH + "/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.customer").exists())
                    .andExpect(jsonPath("$.deliveryOption").exists())
                    .andExpect(jsonPath("$.billingOption").exists())
                    .andExpect(jsonPath("$.created").exists())
                    .andExpect(jsonPath("$.comment").exists())
                    .andExpect(jsonPath("$.shippingAddress").exists())
                    .andExpect(jsonPath("$.billingAddress").exists())
                    .andExpect(jsonPath("$.cart.id").isNumber())
                    .andExpect(jsonPath("$.cart.freeShipping").exists())
                    .andExpect(jsonPath("$.cart.itemCount").isNumber())
                    .andExpect(jsonPath("$.cart.cartPrice").isNumber())
                    .andExpect(jsonPath("$.cart.lines").isArray());

            verify(orderService, times(1)).getOrderById(anyLong());
        });
    }

    @Test
    public void createOrder() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> {
            OrderNewDto orderDto = generateRandomObject(OrderNewDto.class);
            Long orderId = 1L;

            when(orderService.createOrder(orderDto)).thenReturn(orderId);

            mockMvc.perform(post(ORDER_PATH + ORDER_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(orderDto)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/order/" + orderId)))
                    .andExpect(jsonPath("$", is(orderId.intValue())));

            verify(orderService, times(1)).createOrder(orderDto);
        });
    }

    private static String asJsonString(final Object obj) {
        // with 3.0 (or with 2.10 as alternative)
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
