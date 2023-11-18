package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record GroupSetCreateDTO(

        @NonNull
        Long groupId,
        @NonNull
        SetCreateDTO set
) {
}
