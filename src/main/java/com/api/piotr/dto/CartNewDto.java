package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public record CartNewDto(
        @Nullable Long id,
        Boolean freeShipping,
        @Positive Integer itemCount,
        @Positive BigDecimal cartPrice,
        @Valid Set<CartLineNewDto> lines) {
}
