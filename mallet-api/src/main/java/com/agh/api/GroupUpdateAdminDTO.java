package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record GroupUpdateAdminDTO(
        @NonNull
        Long groupId,
        @NonNull
        Long newAdminId
) {
}
