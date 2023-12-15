package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.CART;
import static com.api.piotr.constant.ApiPaths.CART_RECALCULATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.piotr.dto.CartRecalculateDto;
import com.api.piotr.dto.CartRecalculateResultDto;
import com.api.piotr.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(CART)
@Validated
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(path = CART_RECALCULATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CartRecalculateResultDto> recalculateCart(
            @RequestBody @Valid CartRecalculateDto recalculateDto) {
        return accepted().body(cartService.recalculateCart(recalculateDto));
    }
}
