package com.api.piotr.controller;

import static com.api.piotr.constant.ApiPaths.BASE;
import static com.api.piotr.constant.ApiPaths.PAID_OPTION_ITEM_PATH;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.piotr.dto.PaidOptionItemDto;
import com.api.piotr.service.PaidOptionItemService;

@RestController
@RequestMapping(PAID_OPTION_ITEM_PATH)
@Validated
public class PaidOptionItemController {

    private final String CODES = "codes";

    private PaidOptionItemService paidOptionItemService;

    public PaidOptionItemController(PaidOptionItemService paidOptionItemService) {
        this.paidOptionItemService = paidOptionItemService;
    }

    @GetMapping(BASE)
    public ResponseEntity<Page<PaidOptionItemDto>> getAllItemsByPaidOptionCodes(
            Pageable pageable,
            @RequestParam(name = CODES, required = false) String codes) {
        Page<PaidOptionItemDto> items = paidOptionItemService.getAllItemsByPaidOptionCodes(pageable, codes);
        return ok(items);
    }
}
