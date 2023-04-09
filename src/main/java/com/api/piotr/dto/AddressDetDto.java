package com.api.piotr.dto;

public record AddressDetDto(
        Long id,
        String street,
        String houseNumber,
        String zipCode,
        String city,
        String country) {

}
