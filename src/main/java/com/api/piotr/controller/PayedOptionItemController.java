package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.BASE;
import static com.api.piotr.constant.ApiPaths.PAYED_OPTION_ITEM_PATH;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.piotr.dto.PayedOptionItemDto;
import com.api.piotr.service.PayedOptionItemService;

@RestController
@RequestMapping(PAYED_OPTION_ITEM_PATH)
public class PayedOptionItemController {

    private PayedOptionItemService payedOptionItemService;

    public PayedOptionItemController(PayedOptionItemService payedOptionItemService) {
        this.payedOptionItemService = payedOptionItemService;
    }

    @GetMapping(BASE)
    public ResponseEntity<List<PayedOptionItemDto>> getAllItemsByPayedOptionCodes(
            @RequestParam(name = "codes", required = false) String codes) {
        List<PayedOptionItemDto> items = payedOptionItemService.getAllItemsByPayedOptionCodes(codes);
        return ResponseEntity.ok(items);
    }
}
