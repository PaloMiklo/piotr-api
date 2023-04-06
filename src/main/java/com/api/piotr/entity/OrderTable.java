package com.api.piotr.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "order_table")
@AllArgsConstructor
@Data
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "delivery_option_id")
    private PayedOptionItem deliveryOption;

    @ManyToOne
    @JoinColumn(name = "billing_option_id")
    private PayedOptionItem billingOption;

    @Column(name = "created_fe", nullable = false)
    private LocalDateTime created;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "shipping_address")
    private Address shippingAddress;

    @ManyToOne
    @JoinColumn(name = "billing_address")
    private Address billingAddress;
}
