package com.api.piotr.dto;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

public record OrderDetDto(
        Long id,
        CustomerDetDto customer,
        PaidOptionItemDetDto deliveryOption,
        PaidOptionItemDetDto billingOption,
        LocalDateTime createdUi,
        @Nullable String note,
        AddressDetDto shippingAddress,
        AddressDetDto billingAddress,
        CartDetDto cart) {
}
