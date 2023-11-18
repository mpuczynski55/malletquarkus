package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
public record GroupCreateDTO(
        @NonNull
        String name,
        @NonNull
        List<ContributionDTO> contributions
) {

}