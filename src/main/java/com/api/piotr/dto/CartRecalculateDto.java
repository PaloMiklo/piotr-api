package com.api.piotr.dto;

import java.math.BigDecimal;

public record CartRecalculateDto(ProductDetDto product, BigDecimal amount) {
}
