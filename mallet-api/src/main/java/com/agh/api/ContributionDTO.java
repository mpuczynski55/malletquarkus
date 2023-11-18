package com.agh.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ContributionDTO (
        Long id,
        @NonNull
        PermissionType setPermissionType,
        @NonNull
        PermissionType groupPermissionType,
        @NonNull
        UserDTO contributor
){

}
