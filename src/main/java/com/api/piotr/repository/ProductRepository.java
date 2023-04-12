package com.api.piotr.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.ProductDetDto;
import com.api.piotr.dto.ProductRowDto;
import com.api.piotr.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.api.piotr.dto.ProductRowDto(p.id) FROM Product p")
    Page<ProductRowDto> findAllProducts(Pageable pageable);

    @Query("""
            SELECT new com.api.piotr.dto.ProductDetDto(
                prod.id,
                prod.name,
                prod.price,
                prod.description,
                prod.quantity,
                prod.valid
            )
            FROM Product prod
            WHERE prod.id = :id
            """)
    Optional<ProductDetDto> findProductById(@Param("id") Long id);

    @Query("SELECT e FROM Product e WHERE e.id = :id")
    Optional<Product> findProductByIdWithoutJpa(@Param("id") Long id);
}
