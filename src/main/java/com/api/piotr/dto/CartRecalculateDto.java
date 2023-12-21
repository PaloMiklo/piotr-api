package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;

public record CartRecalculateDto(@Valid List<CartRecalculateCartLineDto> cartLines, BigDecimal deliveryPrice) {
}
