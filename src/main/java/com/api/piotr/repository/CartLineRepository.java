package com.api.piotr.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.CartLineDetDto;
import com.api.piotr.entity.CartLine;

public interface CartLineRepository extends JpaRepository<CartLine, Long> {
    @Query("""
            SELECT new com.api.piotr.dto.CartLineDetDto(
                cl.id,
                new com.api.piotr.dto.ProductDetDto(
                    cl.product.id,
                    cl.product.name,
                    cl.product.price,
                    cl.product.description,
                    cl.product.quantity,
                    cl.product.valid
                ),
                cl.amount,
                cl.lineTotal,
                cl.cart.id
                )
            FROM CartLine cl
            WHERE cl.cart.id = :cartId
            """)
    Optional<Set<CartLineDetDto>> findCartLinesByCartId(@Param("cartId") Long cartId);
}
