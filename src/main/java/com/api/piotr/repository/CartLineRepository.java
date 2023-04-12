package com.api.piotr.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.CartLineRowDto;
import com.api.piotr.entity.CartLine;

public interface CartLineRepository extends JpaRepository<CartLine, Long> {
    @Query("""
            SELECT new com.api.piotr.dto.CartLineRowDto(
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
                cl.lineTotal
                )
            FROM CartLine cl
            JOIN OrderTable ordr ON ordr.id = :orderId
            JOIN Cart crt ON crt.id = ordr.cart.id
            WHERE cl.cart = crt.id
            """)
    Optional<Set<CartLineRowDto>> findCartLinesByOrderId(@Param("orderId") Long orderId);
}
