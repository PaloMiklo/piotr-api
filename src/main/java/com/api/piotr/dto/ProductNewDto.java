package com.api.piotr.dto;

import java.math.BigDecimal;
import com.api.piotr.entity.Product;

public record ProductNewDto(String name, BigDecimal price, String description, Integer quantity, byte[] image) {

    public Product toEntity() {
        var product = new Product(null, name, this.price, this.description, this.quantity, null, true);
        return product;
    }
}
