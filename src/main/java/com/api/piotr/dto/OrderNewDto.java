package com.api.piotr.dto;

import java.time.LocalDateTime;

import com.api.piotr.entity.Address;
import com.api.piotr.entity.Customer;
import org.springframework.lang.Nullable;

public record OrderNewDto(
                Customer customer,
                String deliveryOptionItemCode,
                String billingOptionItemCode,
                LocalDateTime created,
                @Nullable String comment,
                Address shippingAddress,
                Address billingAddress) {
}
