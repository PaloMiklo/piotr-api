package com.api.piotr.dto;

import java.math.BigDecimal;

import com.api.piotr.entity.CartLine;
import com.api.piotr.entity.Product;

public record CartLineNewDto(
        Long productId,
        Integer amount,
        BigDecimal lineTotal) {
    public CartLine toEntity(Product product) {
        var cartLine = new CartLine(null, null, amount, lineTotal, null);
        cartLine.setProduct(product);
        return cartLine;
    }
}
