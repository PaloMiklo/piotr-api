package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.PaidOptionItemDto;
import com.api.piotr.entity.PaidOptionItem;

public interface PaidOptionItemRepository extends JpaRepository<PaidOptionItem, Long> {
    @Query("""
             SELECT new com.api.piotr.dto.PaidOptionItemDto(
                item.code,
                item.name,
                item.price
             )
             FROM PaidOptionItem item
             JOIN PaidOption po ON item.paidOption = po.code
             WHERE :codes IS NULL OR po.code IN (:codes)
            """)
    Optional<Page<PaidOptionItemDto>> getAllItemsByPaidOptionCodes(Pageable pageable, @Param("codes") String codes);
}
