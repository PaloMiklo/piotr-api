package com.api.piotr.dto;

import java.math.BigDecimal;

import jakarta.validation.Valid;

public record CartLineRowDto(
                Long id,
                @Valid ProductDetDto product,
                Integer amount,
                BigDecimal lineTotal) {
}
