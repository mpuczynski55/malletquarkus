package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record SetBasicDTO(
        @NonNull
        List<SetInformationDTO> sets,
        String nextChunkUri
) {}