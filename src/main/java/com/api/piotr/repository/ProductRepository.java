package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            SELECT new com.api.piotr.dto.ProductRowDto(p.id) FROM Product p
            """)
    Page<ProductRowDto> findAllProducts(Pageable pageable);

    @Query("""
            SELECT new com.api.piotr.dto.ProductDetDto(p.id) FROM Product p WHERE :id IS NULL OR p.id = :id
                """)
    Optional<ProductDetDto> findProductById(Long id);
}
