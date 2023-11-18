package com.agh.mallet.infrastructure.mapper;

import com.agh.api.GroupBasicInformationDTO;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;

import java.util.Collection;
import java.util.List;

public class GroupBasicInformationDTOMapper {

    private GroupBasicInformationDTOMapper() {}

    public static List<GroupBasicInformationDTO> from(Collection<GroupJPAEntity> groupEntities) {
        return groupEntities.stream()
                .map(GroupBasicInformationDTOMapper::from)
                .toList();
    }

    public static GroupBasicInformationDTO from(GroupJPAEntity groupEntity) {
        return GroupBasicInformationDTO.builder()
                .id(groupEntity.getId())
                .name(groupEntity.getName())
                .identifier(groupEntity.getIdentifier())
                .numberOfSets(groupEntity.getSets().size())
                .numberOfMembers(groupEntity.getContributions().size())
                .build();
    }
}
