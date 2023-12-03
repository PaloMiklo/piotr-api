package com.api.piotr.dto;

import java.math.BigDecimal;

import com.api.piotr.entity.PaidOption;
import com.api.piotr.entity.PaidOptionItem;

public record PaidOptionItemDetDto(
                String code,
                String name,
                String paidOptionItemCode,
                BigDecimal price) {
        public PaidOptionItem toEntity(PaidOption paidOption) {
                return new PaidOptionItem(code, name, paidOption, price);
        }
}
