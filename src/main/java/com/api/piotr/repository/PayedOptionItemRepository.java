package com.api.piotr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.PayedOptionItemDto;
import com.api.piotr.entity.PayedOptionItem;

public interface PayedOptionItemRepository extends JpaRepository<PayedOptionItem, Long> {
    @Query("""
             SELECT new com.api.piotr.dto.PayedOptionItemDto(
                item.code,
                item.name,
                item.price
             )
             FROM PayedOptionItem item
             JOIN PayedOption po ON item.payedOption = po.code
             WHERE :codes IS NULL OR po.code IN (:codes)
            """)
    Optional<List<PayedOptionItemDto>> getAllItemsByPayedOptionCodes(@Param("codes") String codes);
}
