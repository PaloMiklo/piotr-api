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

public interface OrderRepository extends JpaRepository<OrderTable, Long> {
    @Query("SELECT new com.api.piotr.dto.OrderRowDto(o.id) FROM OrderTable o")
    Page<OrderRowDto> findAllOrders(Pageable pageable);

    @Query("""
             SELECT new com.api.piotr.dto.OrderDetDto(o.id)
             FROM OrderTable o
             WHERE o.id = :id
            """)
    Optional<OrderDetDto> findOrderById(@Param("id") Long id);
}
