package com.api.piotr.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "free_shipping", nullable = false)
    private Boolean freeShipping;

    @Column(name = "item_count", nullable = false)
    private Integer itemCount;

    @Column(name = "cart_price", nullable = false)
    private BigDecimal cartPrice;

    @OneToOne(mappedBy = "cart", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private OrderTable orderTable;

    @OneToMany(mappedBy = "cart", orphanRemoval = true)
    private List<CartLine> lines;
}