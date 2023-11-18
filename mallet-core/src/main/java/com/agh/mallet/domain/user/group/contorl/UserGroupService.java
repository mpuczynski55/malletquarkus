package com.agh.mallet.domain.user.group.contorl;

import com.agh.api.GroupBasicDTO;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;
import com.agh.mallet.domain.user.user.control.service.UserService;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import com.agh.mallet.infrastructure.mapper.GroupBasicDTOMapper;
import com.agh.mallet.infrastructure.utils.NextChunkRebuilder;
import jakarta.enterprise.context.Dependent;

import java.security.Principal;
import java.util.List;

@Dependent
public class UserGroupService {

    private final UserService userService;
    private final NextChunkRebuilder nextChunkRebuilder;

    public UserGroupService(UserService userService, NextChunkRebuilder nextChunkRebuilder) {
        this.userService = userService;
        this.nextChunkRebuilder = nextChunkRebuilder;
    }

    public GroupBasicDTO get(int startPosition,
                             int limit,
                             Principal principal) {
        UserJPAEntity userEntity = userService.getByEmail(principal.getName());

        List<GroupJPAEntity> userGroups = userEntity.getUserGroups();

        String nextChunkUri = nextChunkRebuilder.rebuild(userGroups, startPosition, limit);

        return GroupBasicDTOMapper.from(userGroups, nextChunkUri);
    }
}
