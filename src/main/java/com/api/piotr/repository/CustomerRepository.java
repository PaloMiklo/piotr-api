package com.api.piotr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.piotr.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
