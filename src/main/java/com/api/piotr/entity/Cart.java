package com.api.piotr.entity;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "cart")
@AllArgsConstructor
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "free_shipping", nullable = false)
    private Boolean freeShipping;

    @Column(name = "item_count", nullable = false)
    private Integer itemCount;

    @Column(name = "cart_price", nullable = false)
    private BigDecimal cartPrice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderTable orderTable;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartLine> lines;
}