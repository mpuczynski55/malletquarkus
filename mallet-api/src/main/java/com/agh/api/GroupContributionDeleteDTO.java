package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

@Builder
public record GroupContributionDeleteDTO (
        @NonNull
        Long groupId,
        @NonNull
        Set<Long> contributionsToDeleteIds
){
}
