package com.api.piotr.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.piotr.dto.OrderDetDto;
import com.api.piotr.dto.OrderRowDto;
import com.api.piotr.entity.OrderTable;

public interface OrderRepository extends _HibernateRepository<OrderTable>, JpaRepository<OrderTable, Long> {
    @Query("SELECT new com.api.piotr.dto.OrderRowDto(o.id) FROM OrderTable o")
    Page<OrderRowDto> findAllOrders(Pageable pageable);

    @Query("""
            SELECT new com.api.piotr.dto.OrderDetDto(
                ordr.id,
                new com.api.piotr.dto.CustomerDetDto(
                    cust.id,
                    cust.firstName,
                    cust.lastName,
                    cust.email
                ),
                new com.api.piotr.dto.PayedOptionItemDetDto(
                    delOptItem.code,
                    delOptItem.name,
                    delOpt.code,
                    delOptItem.price
                ),
                new com.api.piotr.dto.PayedOptionItemDetDto(
                    billOptItem.code,
                    billOptItem.name,
                    billOpt.code,
                    billOptItem.price
                ),
                ordr.created,
                ordr.comment,
                new com.api.piotr.dto.AddressDetDto(
                    shipAddr.id,
                    shipAddr.street,
                    shipAddr.houseNumber,
                    shipAddr.zipCode,
                    shipAddr.city,
                    shipAddr.country
                ),
                new com.api.piotr.dto.AddressDetDto(
                    billAddr.id,
                    billAddr.street,
                    billAddr.houseNumber,
                    billAddr.zipCode,
                    billAddr.city,
                    billAddr.country
                ),
                new com.api.piotr.dto.CartDetDto(
                    crt.id,
                    crt.freeShipping,
                    crt.itemCount,
                    crt.cartPrice
                )
            )
            FROM OrderTable ordr
            JOIN PayedOptionItem delOptItem ON delOptItem.code = ordr.deliveryOption.code
            JOIN PayedOptionItem billOptItem ON billOptItem.code = ordr.billingOption.code
            JOIN PayedOption delOpt ON delOpt.code = delOptItem.payedOption
            JOIN PayedOption billOpt ON billOpt.code = billOptItem.payedOption
            JOIN Customer cust ON cust.id = ordr.customer.id
            JOIN Cart crt ON ordr.cart.id = crt.id
            JOIN Address shipAddr ON shipAddr.id = ordr.shippingAddress.id
            JOIN Address billAddr ON billAddr.id = ordr.billingAddress.id
            WHERE ordr.id = :id
            """)
    Optional<OrderDetDto> findOrderById(@Param("id") Long id);
}
