package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record GroupSetDTO (
        @NonNull
        Long groupId,
        @NonNull
        Long setId
) {
}
