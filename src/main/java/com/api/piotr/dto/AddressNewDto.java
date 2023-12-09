package com.api.piotr.dto;

import com.api.piotr.entity.Address;

import jakarta.validation.constraints.NotBlank;

public record AddressNewDto(
        @NotBlank String street,
        @NotBlank String houseNumber,
        @NotBlank String zipCode,
        @NotBlank String city,
        @NotBlank String country) {
    public Address toEntity() {
        return new Address(null, street, houseNumber, zipCode, city, country);
    }
}
