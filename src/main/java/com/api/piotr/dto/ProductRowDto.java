package com.api.piotr.dto;

import java.math.BigDecimal;

public record ProductRowDto(
        Long id,
        String name,
        BigDecimal price,
        Boolean valid) {
}
