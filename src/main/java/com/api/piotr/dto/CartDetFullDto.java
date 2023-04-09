package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartDetFullDto(
                Long id,
                Boolean freeShipping,
                Integer itemCount,
                BigDecimal cartPrice,
                Set<CartLineDetDto> lines) {
}
