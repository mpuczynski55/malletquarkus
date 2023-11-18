package com.agh.mallet.infrastructure.mapper;

import com.agh.api.ContributionDTO;
import com.agh.api.PermissionType;
import com.agh.api.UserDTO;
import com.agh.mallet.domain.group.entity.ContributionJPAEntity;

import java.util.Collection;
import java.util.List;

public class ContributionDTOMapper {

    private ContributionDTOMapper() {}

    public static List<ContributionDTO> from(Collection<ContributionJPAEntity> contributions) {
        return contributions.stream()
                .map(ContributionDTOMapper::from)
                .toList();
    }

    public static ContributionDTO from(ContributionJPAEntity contribution) {
        PermissionType setPermissionType = PermissionTypeMapper.from(contribution.getSetPermissionType());
        PermissionType groupPermissionType = PermissionTypeMapper.from(contribution.getGroupPermissionType());
        UserDTO user = UserDTOMapper.from(contribution.getContributor());

        return ContributionDTO.builder()
                .id(contribution.getId())
                .setPermissionType(setPermissionType)
                .groupPermissionType(groupPermissionType)
                .contributor(user)
                .build();
    }
}
