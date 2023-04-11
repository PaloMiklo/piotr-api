package com.api.piotr.dto;

import java.math.BigDecimal;

public record CartLineDetDto(
        Long id,
        ProductDetDto product,
        Integer amount,
        BigDecimal lineTotal,
        Long cartId) {

}
