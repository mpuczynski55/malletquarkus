package com.agh.mallet.infrastructure.mapper;

import com.agh.api.GroupBasicDTO;
import com.agh.api.GroupBasicInformationDTO;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;

import java.util.Collection;
import java.util.List;

public class GroupBasicDTOMapper {

    private GroupBasicDTOMapper() {
    }

    public static GroupBasicDTO from(Collection<GroupJPAEntity> groupEntities, String nextChunkUri) {
        List<GroupBasicInformationDTO> groups = GroupBasicInformationDTOMapper.from(groupEntities);

        return GroupBasicDTO.builder()
                .groups(groups)
                .nextChunkUri(nextChunkUri)
                .build();
    }
}
