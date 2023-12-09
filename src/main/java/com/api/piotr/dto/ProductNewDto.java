package com.api.piotr.dto;

import java.math.BigDecimal;

import com.api.piotr.entity.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductNewDto(
        @NotBlank String name,
        @Positive BigDecimal price,
        @NotBlank String description,
        @PositiveOrZero Integer quantity,
        @Size(min = 1, message = "Image cannot be empty") byte[] image) {

    public Product toEntity() {
        return new Product(name, this.price, this.description, this.quantity);
    }
}
