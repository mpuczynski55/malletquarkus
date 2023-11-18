package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record SetUpdateDTO(
        Long groupId,
        @NonNull
        Long id,
        @NonNull
        String topic,
        String description,
        @NonNull
        List<TermDTO> termsToUpdate,
        List<TermCreateDTO> termsToCreate
) {


}
