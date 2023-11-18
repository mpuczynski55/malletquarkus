package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record UserDetailDTO(
        @NonNull
        Long id,
        @NonNull
        String identifier,
        @NonNull
        String username,
        @NonNull
        LocalDate registrationDate,
        @NonNull
        String email
) {
}
