package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UserRegistrationDTO(
        @NonNull
        String username,
        @NonNull
        String password,
        @NonNull
        String email
) {
}
