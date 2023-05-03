package com.api.piotr.dto;

public record ImageTableRowDto(byte[] image, String mimeType, String fileName) {
}
