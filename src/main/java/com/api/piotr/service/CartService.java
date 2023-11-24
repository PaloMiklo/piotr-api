package com.api.piotr.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.api.piotr.dto.CartRecalculateDto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CartService {
    public BigDecimal recalculateCart(List<CartRecalculateDto> recalculateDtos) {
        return recalculateDtos.stream()
                .map(recalculateDto -> recalculateDto.product().price().multiply(recalculateDto.amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
