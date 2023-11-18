package com.agh.mallet.infrastructure.mapper;

import com.agh.api.SetInformationDTO;
import com.agh.api.UserDTO;
import com.agh.mallet.domain.set.entity.SetJPAEntity;

import java.util.Collection;
import java.util.List;

public class SetInformationDTOMapper {

    private SetInformationDTOMapper() {}

    public static List<SetInformationDTO> from(Collection<SetJPAEntity> sets) {
        return sets.stream()
                .map(SetInformationDTOMapper::from)
                .toList();
    }

    public static SetInformationDTO from(SetJPAEntity set) {
        UserDTO creator = UserDTOMapper.from(set.getCreator());

        return SetInformationDTO.builder()
                .id(set.getId())
                .name(set.getName())
                .creator(creator)
                .identifier(set.getIdentifier())
                .numberOfTerms(set.getTerms().size())
                .description(set.getDescription())
                .build();
    }

}
