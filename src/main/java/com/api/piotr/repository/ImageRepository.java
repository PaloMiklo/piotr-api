
package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.ImageTableRowDto;
import com.api.piotr.entity.ImageTable;

public interface ImageRepository extends _HibernateRepository<ImageTable>, JpaRepository<ImageTable, Long> {
    @Query("""
            SELECT new com.api.piotr.dto.ImageTableRowDto(i.image, i.mimeType, i.fileName)
            FROM ImageTable i
            JOIN Product p ON p.image = i.id
            WHERE :id IS NULL OR p.id = :id
            """)
    Optional<ImageTableRowDto> findImageTableByProductId(@Param("id") Long id);
}
