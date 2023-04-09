package com.api.piotr.dto;

import java.math.BigDecimal;

// TODO add streaming of an image
public record ProductDetDto(
        Long id,
        String name,
        BigDecimal price,
        String description,
        Integer quantity,
        Boolean valid) {
}
