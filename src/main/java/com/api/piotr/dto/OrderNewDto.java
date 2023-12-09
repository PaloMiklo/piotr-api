package com.api.piotr.dto;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

public record OrderNewDto(
                @Valid CustomerNewDto customer,
                @NotBlank String deliveryOptionItemCode,
                @NotBlank String billingOptionItemCode,
                @PastOrPresent LocalDateTime createdUi,
                @Nullable String note,
                @Valid AddressNewDto shippingAddress,
                @Valid AddressNewDto billingAddress,
                @Valid CartNewDto cart) {
}
