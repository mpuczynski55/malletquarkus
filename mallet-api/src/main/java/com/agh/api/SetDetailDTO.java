package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record SetDetailDTO(
        @NonNull
        Long id,
        String name,
        String description,
        @NonNull
        List<TermDTO> terms,
        String nextChunkUri
) {
}
