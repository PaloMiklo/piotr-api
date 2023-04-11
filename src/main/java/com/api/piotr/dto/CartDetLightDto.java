package com.api.piotr.dto;

import java.math.BigDecimal;

public record CartDetLightDto(
                Long id,
                Boolean freeShipping,
                Integer itemCount,
                BigDecimal cartPrice) {
}
