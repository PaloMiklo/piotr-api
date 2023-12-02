package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.CART;
import static com.api.piotr.constant.ApiPaths.CART_RECALCULATE;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.piotr.dto.CartRecalculateDto;
import com.api.piotr.dto.CartRecalculateResultDto;
import com.api.piotr.service.CartService;

@RestController
@RequestMapping(CART)
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(path = CART_RECALCULATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartRecalculateResultDto> recalculateCart(
            @RequestBody CartRecalculateDto recalculateDto) {
        return ResponseEntity.accepted().body(cartService.recalculateCart(recalculateDto));
    }
}
