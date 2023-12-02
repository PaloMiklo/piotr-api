package com.api.piotr.dto;

import java.math.BigDecimal;

public record CartRecalculateResultDto(BigDecimal cartPrice, BigDecimal cartPriceTotal) {
}
