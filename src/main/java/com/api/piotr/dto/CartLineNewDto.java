package com.api.piotr.dto;

import java.math.BigDecimal;

import com.api.piotr.entity.Cart;
import com.api.piotr.entity.CartLine;
import com.api.piotr.entity.Product;

public record CartLineNewDto(
        Long productId,
        Integer amount,
        BigDecimal lineTotal) {
    public CartLine toEntity(Long productId, Cart cart) {
        var cartLine = new CartLine();
        cartLine.setAmount(amount);
        cartLine.setLineTotal(lineTotal);
        cartLine.setProduct(new Product(productId));
        cartLine.setCart(cart);
        return cartLine;
    }
}
