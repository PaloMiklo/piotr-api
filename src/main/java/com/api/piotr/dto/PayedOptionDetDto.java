package com.api.piotr.dto;

import com.api.piotr.entity.PayedOption;

public record PayedOptionDetDto(
        String code,
        String name) {
    public PayedOption toEntity() {
        return new PayedOption(this.code, this.name);
    }
}
