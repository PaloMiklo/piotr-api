package com.api.piotr.dto;

import java.math.BigDecimal;
import com.api.piotr.entity.PayedOption;
import com.api.piotr.entity.PayedOptionItem;

public record PayedOptionItemDetDto(
                String code,
                String name,
                String payedOptionItemCode,
                BigDecimal price) {
        public PayedOptionItem toEntity(PayedOption payedOption) {
                return new PayedOptionItem(code, name, payedOption, price);
        }
}
