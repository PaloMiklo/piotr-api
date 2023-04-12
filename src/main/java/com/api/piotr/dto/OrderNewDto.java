package com.api.piotr.dto;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

public record OrderNewDto(
                CustomerNewDto customer,
                String deliveryOptionItemCode,
                String billingOptionItemCode,
                LocalDateTime created,
                @Nullable String comment,
                AddressNewDto shippingAddress,
                AddressNewDto billingAddress,
                CartNewDto cart) {
}
