package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.CART;
import static com.api.piotr.constant.ApiPaths.CART_RECALCULATE;
import static com.api.piotr.util.MapperUtils.asJsonString;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.piotr.dto.CartRecalculateDto;
import com.api.piotr.dto.CartRecalculateResultDto;
import com.api.piotr.repository.ImageRepository;
import com.api.piotr.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private ImageRepository ImageRepository;

    @Test
    public void recalculateCart() throws Exception {
        assertTimeout(Duration.ofMillis(160), () -> {
            var result = new CartRecalculateResultDto(ONE.add(TEN), TEN.multiply(TEN));

            String json = "{\"cartLines\": [{\"product\": {\"id\": 1,\"name\": \"Product 1\",\"price\": 59.99,\"description\": \"lorem Ipsum.\",\"quantity\": 30,\"valid\": true},\"amount\": 1}],\"deliveryPrice\": 5.99}";
            CartRecalculateDto payload = new ObjectMapper().readValue(json, CartRecalculateDto.class);

            when(cartService.recalculateCart(payload)).thenReturn(result);

            mockMvc.perform(post(CART + CART_RECALCULATE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(asJsonString(payload)))
                    .andExpect(status().isAccepted())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.cartPrice").isNumber())
                    .andExpect(jsonPath("$.cartPriceTotal").isNumber())
                    .andExpect(jsonPath("$.cartPrice").value(valueOf(11)))
                    .andExpect(jsonPath("$.cartPriceTotal").value(valueOf(100)));
        });
    };
}