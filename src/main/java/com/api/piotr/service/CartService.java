package com.api.piotr.service;

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
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartRecalculateResultDto(cartPrice, cartPrice.add(recalculateDto.deliveryPrice()));
    }
}
