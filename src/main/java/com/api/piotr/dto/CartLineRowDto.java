package com.api.piotr.dto;

import java.math.BigDecimal;

public record CartLineRowDto(
        Long id,
        ProductDetDto product,
        Integer amount,
        BigDecimal lineTotal) {
}
