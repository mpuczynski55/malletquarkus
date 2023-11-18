package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record TermBasicListDTO(

        @NonNull
        List<TermDTO> terms,
        String nextChunkUri

) {
}
