package com.api.piotr.dto;

import java.time.LocalDateTime;

import com.api.piotr.entity.Address;
import com.api.piotr.entity.Customer;

public record OrderNewDto(
                Long id,
                Customer customer,
                String deliveryOptionItemCode,
                String billingOptionItemCode,
                LocalDateTime created,
                String comment,
                Address shippingAddress,
                Address billingAddress) {
}
