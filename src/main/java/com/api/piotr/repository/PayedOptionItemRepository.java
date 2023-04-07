package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.PayedOptionItemDetDto;
import com.api.piotr.entity.PayedOptionItem;

public interface PayedOptionItemRepository extends JpaRepository<PayedOptionItem, Long> {
    @Query("""
            SELECT new com.api.piotr.dto.PayedOptionItemDetDto(poi.code, poi.name, poi.payedOption.code, poi.price)
            FROM PayedOptionItem poi
            WHERE poi.code = :code
            """)
    Optional<PayedOptionItemDetDto> findPayedOptionItemByCode(@Param("code") String code);
}
