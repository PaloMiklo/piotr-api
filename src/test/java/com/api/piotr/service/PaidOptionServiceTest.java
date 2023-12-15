package com.api.piotr.service;

import static com.api.piotr.constant.PaidOptions.CART_PAYMENT;
import static com.api.piotr.constant.PaidOptions.CASH_ON_DELIVERY_PAYMENT;
import static com.api.piotr.constant.PaidOptions.GROUND_SHIPPING;
import static com.api.piotr.constant.PaidOptions.OVERNIGHT_SHIPPING_SHIPPING;
import static com.api.piotr.constant.PaidOptions.PAYMENT;
import static com.api.piotr.constant.PaidOptions.SHIPPING;
import static com.api.piotr.constant.PaidOptions.TWO_DAY_SHIPPING_SHIPPING;
import static java.math.BigDecimal.valueOf;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.api.piotr.dto.PaidOptionItemDto;
import com.api.piotr.repository.PaidOptionItemRepository;

@ExtendWith(MockitoExtension.class)
public class PaidOptionServiceTest {

        private static Random random = new Random();

        @Mock
        private PaidOptionItemRepository paidOptionItemRepository;

        @InjectMocks
        private PaidOptionItemService paidOptionItemService;

        @Test
        public void getAllItemsByPaidOptionCodes() throws Exception {
                assertTimeout(Duration.ofMillis(110), () -> {
                        Page<PaidOptionItemDto> items = new PageImpl<PaidOptionItemDto>(List.of(
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

                        when(paidOptionItemRepository.getAllItemsByPaidOptionCodes(any(Pageable.class),
                                        any(String.class)))
                                        .thenReturn(of(items));

                        Page<PaidOptionItemDto> result = paidOptionItemService
                                        .getAllItemsByPaidOptionCodes(Pageable.ofSize(1), codes);

                        verify(paidOptionItemRepository, times(1)).getAllItemsByPaidOptionCodes(Pageable.ofSize(1),
                                        codes);

                        assertEquals(result, items);
                });
        }
}
