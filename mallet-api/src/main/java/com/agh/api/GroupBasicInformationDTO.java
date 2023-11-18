package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record GroupBasicInformationDTO(

        @NonNull
        Long id,
        @NonNull
        String name,
        @NonNull
        String identifier,
        @NonNull
        Integer numberOfSets,
        @NonNull
        Integer numberOfMembers) {
}
