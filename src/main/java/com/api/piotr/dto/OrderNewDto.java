package com.api.piotr.dto;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

public record OrderNewDto(
                CustomerNewDto customer,
                String deliveryOptionItemCode,
                String billingOptionItemCode,
                LocalDateTime createdUi,
                @Nullable String note,
                AddressNewDto shippingAddress,
                AddressNewDto billingAddress,
                CartNewDto cart) {
}
