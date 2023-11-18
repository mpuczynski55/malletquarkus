package com.agh.mallet.infrastructure.mapper;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetInformationDTO;
import com.agh.mallet.domain.set.entity.SetJPAEntity;

import java.util.Collection;
import java.util.List;

public class SetBasicsDTOMapper {

    private SetBasicsDTOMapper() {}

    public static SetBasicDTO from(Collection<SetJPAEntity> userSetEntities, String nextChunkUri) {
        List<SetInformationDTO> userSets = SetInformationDTOMapper.from(userSetEntities);

        return SetBasicDTO.builder()
                .sets(userSets)
                .nextChunkUri(nextChunkUri)
                .build();
    }

    public static SetBasicDTO from(Collection<SetJPAEntity> userSetEntities) {
        List<SetInformationDTO> userSets = SetInformationDTOMapper.from(userSetEntities);

        return SetBasicDTO.builder()
                .sets(userSets)
                .build();
    }

}
