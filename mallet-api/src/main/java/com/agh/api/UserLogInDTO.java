package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UserLogInDTO(
        @NonNull
        String email,
        @NonNull
        String password
) {
}