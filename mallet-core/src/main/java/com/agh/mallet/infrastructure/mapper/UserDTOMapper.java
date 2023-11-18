package com.agh.mallet.infrastructure.mapper;

import com.agh.api.UserDTO;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;

import java.util.Collection;
import java.util.List;

public class UserDTOMapper {

    private UserDTOMapper() {}

    public static List<UserDTO> from(Collection<UserJPAEntity> user) {
        return user.stream()
                .map(UserDTOMapper::from)
                .toList();
    }

    public static UserDTO from(UserJPAEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getUsername())
                .identifier(user.getIdentifier())
                .build();
    }

}
