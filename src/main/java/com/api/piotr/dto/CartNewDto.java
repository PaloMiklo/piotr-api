package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartNewDto(
        Long id,
        Boolean freeShipping,
        Integer itemCount,
        BigDecimal cartPrice,
        Set<CartLineNewDto> lines) {
}
