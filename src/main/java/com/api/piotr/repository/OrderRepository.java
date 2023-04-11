package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.OrderDetLightDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.entity.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, Long> {
    @Query("SELECT new com.api.piotr.dto.OrderRowDto(o.id) FROM OrderTable o")
    Page<OrderRowDto> findAllOrders(Pageable pageable);

    @Query("""
            SELECT new com.api.piotr.dto.OrderDetLightDto(
                ord.id,
                new com.api.piotr.dto.CustomerDetDto(
                    ord.customer.id,
                    ord.customer.firstName,
                    ord.customer.lastName,
                    ord.customer.email
                ),
                new com.api.piotr.dto.PayedOptionItemDetDto(
                    ord.deliveryOption.code,
                    ord.deliveryOption.name,
                    shippingOpt.code,
                    ord.deliveryOption.price
                ),
                new com.api.piotr.dto.PayedOptionItemDetDto(
                    ord.billingOption.code,
                    ord.billingOption.name,
                    billingOpt.code,
                    ord.billingOption.price
                ),
                ord.created,
                ord.comment,
                new com.api.piotr.dto.AddressDetDto(
                    ord.shippingAddress.id,
                    ord.shippingAddress.street,
                    ord.shippingAddress.houseNumber,
                    ord.shippingAddress.zipCode,
                    ord.shippingAddress.city,
                    ord.shippingAddress.country
                ),
                new com.api.piotr.dto.AddressDetDto(
                    ord.billingAddress.id,
                    ord.billingAddress.street,
                    ord.billingAddress.houseNumber,
                    ord.billingAddress.zipCode,
                    ord.billingAddress.city,
                    ord.billingAddress.country
                ),
                new com.api.piotr.dto.CartDetLightDto(
                    ord.cart.id,
                    ord.cart.freeShipping,
                    ord.cart.itemCount,
                    ord.cart.cartPrice
                )
            )
            FROM OrderTable ord
            JOIN PayedOptionItem poi1 ON o.deliveryOption.code = poi1.code
            JOIN PayedOption shippingOpt ON po1.code = poi1.payedOption.code
            JOIN PayedOptionItem poi2 ON o.billingOption.code = poi2.code
            JOIN PayedOption billingOpt ON po2.code = poi2.payedOption.code
            WHERE o.id = :id
            """)
    Optional<OrderDetLightDto> findOrderById(@Param("id") Long id);
}
