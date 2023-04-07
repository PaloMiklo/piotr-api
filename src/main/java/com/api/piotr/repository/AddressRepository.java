package com.api.piotr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.piotr.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
