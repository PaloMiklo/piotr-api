package com.api.piotr.dto;

import com.api.piotr.entity.Address;

public record AddressNewDto(
        String street,
        String houseNumber,
        String zipCode,
        String city,
        String country) {
    public Address toEntity() {
        return new Address(null, street, houseNumber, zipCode, city, country);
    }
}
