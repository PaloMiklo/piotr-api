package com.api.piotr.service;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.api.piotr.dto.CartRecalculateDto;
import com.api.piotr.dto.CartRecalculateResultDto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CartService {
    public CartRecalculateResultDto recalculateCart(CartRecalculateDto recalculateDto) {
        var cartPrice = recalculateDto.cartLines().stream()
                .map(cartLine -> cartLine.product().price().multiply(cartLine.amount()))
                .reduce(ZERO, BigDecimal::add);
        // TODO: correct the shipping cost based on the free shipping
        return new CartRecalculateResultDto(cartPrice, cartPrice.add(recalculateDto.deliveryPrice()));
    }
}
