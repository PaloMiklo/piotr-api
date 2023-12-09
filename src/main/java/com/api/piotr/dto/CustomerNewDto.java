package com.api.piotr.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerNewDto(
                @NotBlank String firstName,
                @NotBlank String lastName,
                @NotBlank String email) {
}
