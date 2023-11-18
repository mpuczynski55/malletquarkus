package com.agh.api;


import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
public record GroupUpdateDTO(
        @NonNull
        Long id,
        String name,
        @NonNull
        List<ContributionDTO> contributions
) {
}
