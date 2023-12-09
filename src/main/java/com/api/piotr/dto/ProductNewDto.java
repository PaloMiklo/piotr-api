package com.api.piotr.dto;

import java.math.BigDecimal;

import com.api.piotr.entity.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductNewDto(
        @NotBlank String name,
        @Positive BigDecimal price,
        @NotBlank String description,
        @PositiveOrZero Integer quantity,
        @NotBlank byte[] image) {

    public Product toEntity() {
        return new Product(name, this.price, this.description, this.quantity);
    }
}
