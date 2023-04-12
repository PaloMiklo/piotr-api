package com.api.piotr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.piotr.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
