package com.api.piotr.dto;

import static com.api.piotr.dsl.IdReference.createIdRef;

import java.math.BigDecimal;

import com.api.piotr.entity.Cart;
import com.api.piotr.entity.CartLine;
import com.api.piotr.entity.Product;

import jakarta.validation.constraints.PositiveOrZero;

public record CartLineNewDto(
        Long productId,
        @PositiveOrZero BigDecimal productPrice,
        @PositiveOrZero Integer amount) {
    public CartLine toEntity(Cart cart) {
        var cartLine = new CartLine();
        cartLine.setAmount(amount);
        cartLine.setLineTotal(productPrice.multiply(BigDecimal.valueOf(amount)));
        cartLine.setProduct(createIdRef(Product.class, productId));
        cartLine.setCart(cart);
        return cartLine;
    }
}
