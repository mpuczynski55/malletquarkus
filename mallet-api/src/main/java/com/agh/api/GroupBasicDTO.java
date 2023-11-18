package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record GroupBasicDTO(
        @NonNull
        List<GroupBasicInformationDTO> groups,
        String nextChunkUri) {
}
