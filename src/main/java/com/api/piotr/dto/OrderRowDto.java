package com.api.piotr.dto;

import java.time.LocalDateTime;

public record OrderRowDto(
                Long id,
                CustomerDetDto customer,
                LocalDateTime created) {
}
