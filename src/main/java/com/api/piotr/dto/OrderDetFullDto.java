package com.api.piotr.dto;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

public record OrderDetFullDto(
        Long id,
        CustomerDetDto customer,
        PayedOptionItemDetDto deliveryOption,
        PayedOptionItemDetDto billingOption,
        LocalDateTime created,
        @Nullable String comment,
        AddressDetDto shippingAddress,
        AddressDetDto billingAddress,
        CartDetFullDto cart) {

}
