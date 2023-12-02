package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartRecalculateDto(List<CartRecalculateCartLineDto> cartLines, BigDecimal deliveryPrice) {
}
