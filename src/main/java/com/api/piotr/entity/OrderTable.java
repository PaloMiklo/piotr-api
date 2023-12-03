package com.api.piotr.entity;

import java.time.LocalDateTime;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "order_table")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "delivery_option_item_code", nullable = false)
    private PaidOptionItem deliveryOption;

    @ManyToOne
    @JoinColumn(name = "billing_option_item_code", nullable = false)
    private PaidOptionItem billingOption;

    @Column(name = "created_fe", nullable = false)
    private LocalDateTime created;

    @Nullable
    private String comment;

    @ManyToOne
    @JoinColumn(name = "shipping_address", nullable = false)
    private Address shippingAddress;

    @ManyToOne
    @JoinColumn(name = "billing_address", nullable = false)
    private Address billingAddress;

    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "cart", nullable = false)
    private Cart cart;
}
