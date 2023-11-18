package com.agh.mallet.infrastructure.mapper;

import com.agh.api.ContributionDTO;
import com.agh.api.GroupDTO;
import com.agh.api.SetInformationDTO;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;

import java.util.List;

public class GroupDTOMapper {

    private GroupDTOMapper() {}

    public static GroupDTO from(GroupJPAEntity groupJPAEntity){
        List<ContributionDTO> contributions = ContributionDTOMapper.from(groupJPAEntity.getContributions());
        List<SetInformationDTO> sets = SetInformationDTOMapper.from(groupJPAEntity.getSets());

        return GroupDTO.builder()
                .id(groupJPAEntity.getId())
                .name(groupJPAEntity.getName())
                .contributions(contributions)
                .sets(sets)
                .build();
    }

}
