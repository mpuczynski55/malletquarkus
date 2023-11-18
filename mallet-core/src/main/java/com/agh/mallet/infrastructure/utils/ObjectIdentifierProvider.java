package com.agh.mallet.infrastructure.utils;

import com.agh.mallet.domain.group.control.GroupRepository;
import com.agh.mallet.domain.set.control.repository.SetRepository;
import com.agh.mallet.domain.user.user.control.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ObjectIdentifierProvider {

    private static final String HASHTAG = "#";
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final SetRepository setRepository;

    public ObjectIdentifierProvider(UserRepository userRepository, GroupRepository groupRepository, SetRepository setRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.setRepository = setRepository;
    }

    public String fromUsername(String username){
        long userCount = userRepository.countAllByUsername(username);

        return trimWhitespaces(username + HASHTAG + userCount);
    }

    public String fromGroupName(String groupName){
        long userCount = groupRepository.countAllByName(groupName);

        return trimWhitespaces(groupName + HASHTAG + userCount);
    }

    public String fromSetName(String setName) {
        long userCount = setRepository.countAllByName(setName);

        return trimWhitespaces(setName + HASHTAG + userCount);
    }

    private String trimWhitespaces(String value) {
        return value.replaceAll("\\s", "");
    }
}