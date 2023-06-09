package com.api.piotr.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 255, nullable = false)
    private String street;

    @Column(name = "house_number", length = 255, nullable = false)
    private String houseNumber;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    @Column(length = 255, nullable = false)
    private String city;

    @Column(length = 255, nullable = false)
    private String country;
}
