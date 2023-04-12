package com.api.piotr.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartDetDto(
                Long id,
                Boolean freeShipping,
                Integer itemCount,
                BigDecimal cartPrice,
                Set<CartLineRowDto> lines) {
        public CartDetDto(
                        Long id,
                        Boolean freeShipping,
                        Integer itemCount,
                        BigDecimal cartPrice,
                        Set<CartLineRowDto> lines) {
                this.id = id;
                this.freeShipping = freeShipping;
                this.itemCount = itemCount;
                this.cartPrice = cartPrice;
                this.lines = lines;
        }

        public CartDetDto(
                        Long id,
                        Boolean freeShipping,
                        Integer itemCount,
                        BigDecimal cartPrice) {
                this(id, freeShipping, itemCount, cartPrice, null);
        }
}
