package com.api.piotr.entity;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.lang.Nullable;

import com.api.piotr.dsl.IdReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product implements IdReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Nullable
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private ImageTable image;

    @Column(nullable = false)
    private Boolean valid = false;

    @OneToMany(mappedBy = "product")
    private Set<CartLine> cartLines;

    public Product(String name, BigDecimal price, String description, Integer quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.valid = true;
    }
}
