package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record TermCreateDTO (
        @NonNull
        String term,
        String definition,
        @NonNull
        Language language,
        TermCreateDTO translation
) {
}
