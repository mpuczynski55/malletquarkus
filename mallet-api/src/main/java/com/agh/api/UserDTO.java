package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UserDTO(
        @NonNull
        Long id,
        String name,
        String identifier
) {
}
