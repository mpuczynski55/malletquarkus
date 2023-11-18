package com.agh.api;

import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorResponseDTO(

        Date timestamp,
        int httpCode,
        String httpStatus,
        String message,
        String stackTrace) {
}
