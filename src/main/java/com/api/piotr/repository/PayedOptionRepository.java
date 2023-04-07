package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.PayedOptionDetDto;
import com.api.piotr.entity.PayedOption;

public interface PayedOptionRepository extends JpaRepository<PayedOption, Long> {
    @Query("""
                SELECT new com.api.piotr.dto.PayedOptionDetDto(po.code, po.name)
                FROM PayedOption po
                JOIN PayedOptionItem poi ON poi.payedOption.code = po.code
                WHERE poi.code = :code
            """)
    Optional<PayedOptionDetDto> findPayedOptionByItemCode(@Param("code") String code);
}
