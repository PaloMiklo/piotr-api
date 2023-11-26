package com.api.piotr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.piotr.dto.PayedOptionItemDto;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.PayedOptionItemRepository;

import io.micrometer.common.lang.Nullable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PayedOptionItemService {

    private final PayedOptionItemRepository payedOptionItemRepository;

    public Page<PayedOptionItemDto> getAllItemsByPayedOptionCodes(Pageable pageable, @Nullable String codes) {
        return payedOptionItemRepository.getAllItemsByPayedOptionCodes(pageable, codes)
                .orElseThrow(() -> new ResourceNotFoundException("PayedOption", String.valueOf(codes)));
    }
}
