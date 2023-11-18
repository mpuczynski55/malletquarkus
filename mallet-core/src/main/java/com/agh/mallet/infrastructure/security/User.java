package com.agh.mallet.infrastructure.security;

import lombok.Builder;

@Builder
public record User (
        Long id,
        String username,
        String password,
        String email,
        boolean enabled
) {


}
