package com.api.piotr.dto;

import java.time.Instant;

import org.springframework.lang.Nullable;

public record OrderDetDto(
                Long id,
                CustomerDetDto customer,
                PaidOptionItemDetDto deliveryOption,
                PaidOptionItemDetDto billingOption,
                Instant createdUi,
                @Nullable String note,
                AddressDetDto shippingAddress,
                AddressDetDto billingAddress,
                CartDetDto cart) {
}
