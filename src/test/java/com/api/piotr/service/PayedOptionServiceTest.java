package com.api.piotr.service;

import static com.api.piotr.constant.PayedOptions.CART_PAYMENT;
import static com.api.piotr.constant.PayedOptions.CASH_ON_DELIVERY_PAYMENT;
import static com.api.piotr.constant.PayedOptions.GROUND_SHIPPING;
import static com.api.piotr.constant.PayedOptions.OVERNIGHT_SHIPPING_SHIPPING;
import static com.api.piotr.constant.PayedOptions.PAYMENT;
import static com.api.piotr.constant.PayedOptions.SHIPPING;
import static com.api.piotr.constant.PayedOptions.TWO_DAY_SHIPPING_SHIPPING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.piotr.dto.PayedOptionItemDto;
import com.api.piotr.repository.PayedOptionItemRepository;

@ExtendWith(MockitoExtension.class)
public class PayedOptionServiceTest {

    @Mock
    private PayedOptionItemRepository payedOptionItemRepository;

    @InjectMocks
    private PayedOptionItemService payedOptionItemService;

    private static Random random = new Random();

    @Test
    public void getAllItemsByPayedOptionCodes() throws Exception {
        assertTimeout(Duration.ofMillis(110), () -> {
            List<PayedOptionItemDto> items = List.of(
                    new PayedOptionItemDto(CART_PAYMENT, CART_PAYMENT, BigDecimal.valueOf(random.nextDouble())),
                    new PayedOptionItemDto(CASH_ON_DELIVERY_PAYMENT, CASH_ON_DELIVERY_PAYMENT,
                            BigDecimal.valueOf(random.nextDouble())),
                    new PayedOptionItemDto(TWO_DAY_SHIPPING_SHIPPING, TWO_DAY_SHIPPING_SHIPPING,
                            BigDecimal.valueOf(random.nextDouble())),
                    new PayedOptionItemDto(GROUND_SHIPPING, GROUND_SHIPPING, BigDecimal.valueOf(random.nextDouble())),
                    new PayedOptionItemDto(OVERNIGHT_SHIPPING_SHIPPING, OVERNIGHT_SHIPPING_SHIPPING,
                            BigDecimal.valueOf(random.nextDouble())));

            String codes = String.format("%s,%s", SHIPPING, PAYMENT);

            when(payedOptionItemRepository.getAllItemsByPayedOptionCodes(any(String.class)))
                    .thenReturn(Optional.of(items));

            List<PayedOptionItemDto> result = payedOptionItemService.getAllItemsByPayedOptionCodes(codes);

            verify(payedOptionItemRepository, times(1)).getAllItemsByPayedOptionCodes(codes);

            assertEquals(result, items);
        });
    }
}
