package com.api.piotr.controller;

import static com.api.piotr.constant.PayedOptions.CART_PAYMENT;
import static com.api.piotr.constant.PayedOptions.CASH_ON_DELIVERY_PAYMENT;
import static com.api.piotr.constant.PayedOptions.GROUND_SHIPPING;
import static com.api.piotr.constant.PayedOptions.OVERNIGHT_SHIPPING_SHIPPING;
import static com.api.piotr.constant.PayedOptions.PAYMENT;
import static com.api.piotr.constant.PayedOptions.SHIPPING;
import static com.api.piotr.constant.PayedOptions.TWO_DAY_SHIPPING_SHIPPING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.api.piotr.constant.ApiPaths;
import com.api.piotr.dto.PayedOptionItemDto;
import com.api.piotr.service.PayedOptionItemService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PayedOptionItemController.class)
public class PayedOptionItemControllerTest {

        private static Random random = new Random();

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PayedOptionItemService payedOptionItemService;
        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        public void getAllItemsByPayedOptionCodes() throws Exception {
                assertTimeout(Duration.ofMillis(110), () -> {
                        Page<PayedOptionItemDto> items = new PageImpl<>(List.of(
                                        new PayedOptionItemDto(CART_PAYMENT, CART_PAYMENT,
                                                        BigDecimal.valueOf(random.nextDouble())),
                                        new PayedOptionItemDto(CASH_ON_DELIVERY_PAYMENT, CASH_ON_DELIVERY_PAYMENT,
                                                        BigDecimal.valueOf(random.nextDouble())),
                                        new PayedOptionItemDto(TWO_DAY_SHIPPING_SHIPPING, TWO_DAY_SHIPPING_SHIPPING,
                                                        BigDecimal.valueOf(random.nextDouble())),
                                        new PayedOptionItemDto(GROUND_SHIPPING, GROUND_SHIPPING,
                                                        BigDecimal.valueOf(random.nextDouble())),
                                        new PayedOptionItemDto(OVERNIGHT_SHIPPING_SHIPPING, OVERNIGHT_SHIPPING_SHIPPING,
                                                        BigDecimal.valueOf(random.nextDouble()))));

                        String codes = String.format("%s,%s", SHIPPING, PAYMENT);

                        given(payedOptionItemService.getAllItemsByPayedOptionCodes(any(Pageable.class), any()))
                                        .willReturn(items);

                        MvcResult result = mockMvc.perform(get(ApiPaths.PAYED_OPTION_ITEM_PATH)
                                        .param(codes, codes))
                                        .andExpect(status().isOk())
                                        .andReturn();

                        String resultBody = result.getResponse().getContentAsString();

                        List<PayedOptionItemDto> itemsObtained = objectMapper.readValue(resultBody,
                                        new TypeReference<List<PayedOptionItemDto>>() {
                                        });

                        assertThat(itemsObtained.size()).isEqualTo(5);
                });
        }
}
