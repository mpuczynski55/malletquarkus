package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record GroupDTO(
        @NonNull
        Long id,
        @NonNull
        String name,
        @NonNull
        List<ContributionDTO> contributions,
        @NonNull
        List<SetInformationDTO> sets
) {

}