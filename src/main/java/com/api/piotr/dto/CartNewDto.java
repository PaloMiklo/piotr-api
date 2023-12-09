package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.annotation.Nullable;

public record CartNewDto(
                @Nullable Long id,
                Boolean freeShipping,
                Integer itemCount,
                BigDecimal cartPrice,
                Set<CartLineNewDto> lines) {
}
