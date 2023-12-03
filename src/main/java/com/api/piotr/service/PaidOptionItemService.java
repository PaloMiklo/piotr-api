package com.api.piotr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.piotr.dto.PaidOptionItemDto;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.PaidOptionItemRepository;

import io.micrometer.common.lang.Nullable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PaidOptionItemService {

    private final PaidOptionItemRepository paidOptionItemRepository;

    public Page<PaidOptionItemDto> getAllItemsByPaidOptionCodes(Pageable pageable, @Nullable String codes) {
        return paidOptionItemRepository.getAllItemsByPaidOptionCodes(pageable, codes)
                .orElseThrow(() -> new ResourceNotFoundException("PaidOption", String.valueOf(codes)));
    }
}
