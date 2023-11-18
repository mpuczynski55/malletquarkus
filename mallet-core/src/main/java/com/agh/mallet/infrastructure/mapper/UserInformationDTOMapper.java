package com.agh.mallet.infrastructure.mapper;

import com.agh.api.UserDetailDTO;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;

public class UserInformationDTOMapper {

    private UserInformationDTOMapper() {
    }

    public static UserDetailDTO from(UserJPAEntity user) {
        return UserDetailDTO.builder()
                .id(user.getId())
                .identifier(user.getIdentifier())
                .username(user.getUsername())
                .registrationDate(user.getRegistrationDate())
                .email(user.getEmail())
                .build();
    }
}
