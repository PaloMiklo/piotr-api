package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.BASE;
import static com.api.piotr.constant.ApiPaths.PAYED_OPTION_ITEM_PATH;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final String CODES = "codes";

    private PayedOptionItemService payedOptionItemService;

    public PayedOptionItemController(PayedOptionItemService payedOptionItemService) {
        this.payedOptionItemService = payedOptionItemService;
    }

    @GetMapping(BASE)
    public ResponseEntity<Page<PayedOptionItemDto>> getAllItemsByPayedOptionCodes(
            Pageable pageable,
            @RequestParam(name = CODES, required = false) String codes) {
        Page<PayedOptionItemDto> items = payedOptionItemService.getAllItemsByPayedOptionCodes(pageable, codes);
        return ResponseEntity.ok(items);
    }
}
