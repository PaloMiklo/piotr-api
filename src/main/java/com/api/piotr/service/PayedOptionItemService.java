package com.api.piotr.service;

import java.util.List;

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

    public List<PayedOptionItemDto> getAllItemsByPayedOptionCodes(@Nullable String codes) {
        return payedOptionItemRepository.getAllItemsByPayedOptionCodes(codes)
                .orElseThrow(() -> new ResourceNotFoundException("PayedOption", String.valueOf(codes)));
    }
}
