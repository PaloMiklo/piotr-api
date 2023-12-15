package com.api.piotr.service;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.piotr.dto.CartRecalculateDto;
import com.api.piotr.dto.CartRecalculateResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Test
    public void recalculateCart_paidDelivery() throws Exception {
        assertTimeout(Duration.ofMillis(150), () -> {
            CartRecalculateDto payload = new ObjectMapper().readValue(
                    "{" +
                            "\"cartLines\":[" +
                            "{" +
                            "\"product\":{" +
                            "\"id\":1," +
                            "\"name\":\"Product 1\"," +
                            "\"price\":10.01," +
                            "\"description\":\"lorem Ipsum.\"," +
                            "\"quantity\":30," +
                            "\"valid\":true" +
                            "}," +
                            "\"amount\":1" +
                            "}" +
                            "]," +
                            "\"deliveryPrice\":5.01" +
                            "}",
                    CartRecalculateDto.class);

            CartRecalculateResultDto result = cartService.recalculateCart(payload);

            assertEquals(valueOf(10.01), valueOf(result.cartPrice()));
            assertEquals(valueOf(15.02), valueOf(result.cartPriceTotal()));
        });
    }

    @Test
    public void recalculateCart_withNullCartlines() throws Exception {
        CartRecalculateDto payload = new ObjectMapper().readValue(
                "{\"cartLines\":" + null + ",\"deliveryPrice\":5.99}",
                CartRecalculateDto.class);
        assertThrows(Exception.class, () -> {
            cartService.recalculateCart(payload);
        });
    }

    @Test
    public void recalculateCart_withNullDeliveryPrice() throws Exception {
        CartRecalculateDto payload = new ObjectMapper().readValue(
                "{" +
                        "\"cartLines\":[" +
                        "{" +
                        "\"product\":{" +
                        "\"id\":1," +
                        "\"name\":\"Product 1\"," +
                        "\"price\":59.99," +
                        "\"description\":\"lorem Ipsum.\"," +
                        "\"quantity\":30," +
                        "\"valid\":true" +
                        "}," +
                        "\"amount\":1" +
                        "}" +
                        "]," +
                        "\"deliveryPrice\":null" +
                        "}",
                CartRecalculateDto.class);
        assertThrows(Exception.class, () -> {
            cartService.recalculateCart(payload);
        });
    }

    @Test
    public void recalculateCart_withNullPayload() throws Exception {
        assertThrows(Exception.class, () -> {
            cartService.recalculateCart(null);
        });
    }
}
