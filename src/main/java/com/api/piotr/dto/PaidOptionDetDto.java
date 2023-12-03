package com.api.piotr.dto;

import com.api.piotr.entity.PaidOption;

public record PaidOptionDetDto(
        String code,
        String name) {
    public PaidOption toEntity() {
        return new PaidOption(this.code, this.name);
    }
}
