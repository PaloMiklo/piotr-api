package com.api.piotr.dto;

import java.math.BigDecimal;

public record PayedOptionItemDto(
        String code,
        String name,
        BigDecimal price) {
}
