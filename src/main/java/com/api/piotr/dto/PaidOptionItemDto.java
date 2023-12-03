package com.api.piotr.dto;

import java.math.BigDecimal;

public record PaidOptionItemDto(
                String code,
                String name,
                BigDecimal price) {
}
