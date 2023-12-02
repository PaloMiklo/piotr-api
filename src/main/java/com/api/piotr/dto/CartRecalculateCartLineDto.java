package com.api.piotr.dto;

import java.math.BigDecimal;

public record CartRecalculateCartLineDto(ProductDetDto product, BigDecimal amount) {
}
