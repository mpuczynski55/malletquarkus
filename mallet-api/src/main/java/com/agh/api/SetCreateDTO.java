package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record SetCreateDTO(
        @NonNull
        String topic,
        String description,
        @NonNull
        List<Long> existingTermIds,
        List<TermCreateDTO> termsToCreate
) {
}
