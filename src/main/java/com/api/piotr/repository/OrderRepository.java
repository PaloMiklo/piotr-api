package com.api.piotr.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.CartLineDetDto;
import com.api.piotr.dto.OrderDetLightDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.entity.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, Long> {
    @Query("SELECT new com.api.piotr.dto.OrderRowDto(o.id) FROM OrderTable o")
    Page<OrderRowDto> findAllOrders(Pageable pageable);

    @Query("""
            SELECT new com.api.piotr.dto.OrderDetLightDto(
                o.id,
                new com.api.piotr.dto.CustomerDetDto(
                    o.customer.id, o.customer.firstName, o.customer.lastName, o.customer.email
                ),
                new com.api.piotr.dto.PayedOptionItemDetDto(
                    o.deliveryOption.code,
                    o.deliveryOption.name,
                    po1.code,
                    o.deliveryOption.price
                ),
                new com.api.piotr.dto.PayedOptionItemDetDto(
                    o.billingOption.code,
                    o.billingOption.name,
                    po2.code,
                    o.billingOption.price
                ),
                o.created,
                o.comment,
                new com.api.piotr.dto.AddressDetDto(
                    o.shippingAddress.id,
                    o.shippingAddress.street,
                    o.shippingAddress.houseNumber,
                    o.shippingAddress.zipCode,
                    o.shippingAddress.city,
                    o.shippingAddress.country
                ),
                new com.api.piotr.dto.AddressDetDto(
                    o.billingAddress.id,
                    o.billingAddress.street,
                    o.billingAddress.houseNumber,
                    o.billingAddress.zipCode,
                    o.billingAddress.city,
                    o.billingAddress.country
                ),
                new com.api.piotr.dto.CartDetLightDto(
                    o.cart.id,
                    o.cart.freeShipping,
                    o.cart.itemCount,
                    o.cart.cartPrice
                )
            )
            FROM OrderTable o
            JOIN PayedOptionItem poi1 ON o.deliveryOption.code = poi1.code
            JOIN PayedOption po1 ON po1.code = poi1.payedOption.code
            JOIN PayedOptionItem poi2 ON o.billingOption.code = poi2.code
            JOIN PayedOption po2 ON po2.code = poi2.payedOption.code
            WHERE o.id = :id
            """)
    Optional<OrderDetLightDto> findOrderById(@Param("id") Long id);

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
            WHERE cl.cart.id = :id
            """)
    Optional<Set<CartLineDetDto>> findCartLinesByCartId(@Param("id") Long id);
}
