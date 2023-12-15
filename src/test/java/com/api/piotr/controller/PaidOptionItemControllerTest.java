package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.PAID_OPTION_ITEM_PATH;
import static com.api.piotr.constant.PaidOptions.CART_PAYMENT;
import static com.api.piotr.constant.PaidOptions.CASH_ON_DELIVERY_PAYMENT;
import static com.api.piotr.constant.PaidOptions.GROUND_SHIPPING;
import static com.api.piotr.constant.PaidOptions.OVERNIGHT_SHIPPING_SHIPPING;
import static com.api.piotr.constant.PaidOptions.PAYMENT;
import static com.api.piotr.constant.PaidOptions.SHIPPING;
import static com.api.piotr.constant.PaidOptions.TWO_DAY_SHIPPING_SHIPPING;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.api.piotr.dto.PaidOptionItemDto;
import com.api.piotr.repository.ImageRepository;
import com.api.piotr.service.PaidOptionItemService;

@WebMvcTest(PaidOptionItemController.class)
public class PaidOptionItemControllerTest {

        private static Random random = new Random();

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PaidOptionItemService paidOptionItemService;

        @MockBean
        private ImageRepository ImageRepository;

        @Test
        public void getAllItemsByPaidOptionCodes() throws Exception {
                assertTimeout(Duration.ofMillis(110), () -> {
                        Page<PaidOptionItemDto> items = new PageImpl<>(List.of(
                                        new PaidOptionItemDto(CART_PAYMENT,
                                                        CART_PAYMENT,
                                                        valueOf(random.nextDouble())),
                                        new PaidOptionItemDto(CASH_ON_DELIVERY_PAYMENT,
                                                        CASH_ON_DELIVERY_PAYMENT,
                                                        valueOf(random.nextDouble())),
                                        new PaidOptionItemDto(TWO_DAY_SHIPPING_SHIPPING,
                                                        TWO_DAY_SHIPPING_SHIPPING,
                                                        valueOf(random.nextDouble())),
                                        new PaidOptionItemDto(GROUND_SHIPPING,
                                                        GROUND_SHIPPING,
                                                        valueOf(random.nextDouble())),
                                        new PaidOptionItemDto(OVERNIGHT_SHIPPING_SHIPPING,
                                                        OVERNIGHT_SHIPPING_SHIPPING,
                                                        valueOf(random.nextDouble()))));

                        String codes = String.format("%s,%s", SHIPPING, PAYMENT);

                        given(paidOptionItemService.getAllItemsByPaidOptionCodes(any(Pageable.class), any()))
                                        .willReturn(items);

                        mockMvc.perform(get(PAID_OPTION_ITEM_PATH)
                                        .param(codes, codes))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content", hasSize(5)))
                                        .andExpect(jsonPath("$.content[0].code", is(CART_PAYMENT)))
                                        .andExpect(jsonPath("$.content[1].code", is(CASH_ON_DELIVERY_PAYMENT)));

                        verify(paidOptionItemService, times(1))
                                        .getAllItemsByPaidOptionCodes(any(Pageable.class), any());

                        verifyNoMoreInteractions(paidOptionItemService);
                });
        }
}
