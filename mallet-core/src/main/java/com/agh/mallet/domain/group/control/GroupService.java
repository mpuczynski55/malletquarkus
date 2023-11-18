package com.agh.mallet.domain.group.control;

import com.agh.api.ContributionDTO;
import com.agh.api.GroupContributionDeleteDTO;
import com.agh.api.GroupCreateDTO;
import com.agh.api.GroupDTO;
import com.agh.api.GroupSetCreateDTO;
import com.agh.api.GroupSetDTO;
import com.agh.api.GroupUpdateAdminDTO;
import com.agh.api.GroupUpdateDTO;
import com.agh.api.SetCreateDTO;
import com.agh.api.TermCreateDTO;
import com.agh.api.UserDTO;
import com.agh.mallet.domain.group.entity.ContributionJPAEntity;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;
import com.agh.mallet.domain.group.entity.PermissionType;
import com.agh.mallet.domain.set.control.service.SetService;
import com.agh.mallet.domain.set.entity.SetJPAEntity;
import com.agh.mallet.domain.term.control.repository.TermRepository;
import com.agh.mallet.domain.term.entity.Language;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import com.agh.mallet.domain.user.user.control.repository.UserRepository;
import com.agh.mallet.domain.user.user.control.service.UserService;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import com.agh.mallet.infrastructure.exception.MalletNotFoundException;
import com.agh.mallet.infrastructure.mapper.GroupDTOMapper;
import com.agh.mallet.infrastructure.mapper.PermissionTypeMapper;
import com.agh.mallet.infrastructure.utils.ObjectIdentifierProvider;
import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Dependent
public class GroupService {

    private static final String INSUFFICIENT_PERMISSION_PREFIX_MSG = "Insufficient permission to ";
    private static final String PERMISSION_EDIT_GROUP_ERROR_MSG = INSUFFICIENT_PERMISSION_PREFIX_MSG + "edit group";
    private static final String PERMISSION_REMOVE_GROUP_ERROR_MSG = INSUFFICIENT_PERMISSION_PREFIX_MSG + "remove group";
    private static final String PERMISSION_CHANGE_ADMIN_ERROR_MSG = INSUFFICIENT_PERMISSION_PREFIX_MSG + "change admin";
    private static final String PERMISSION_ADD_SET_ERROR_MSG = INSUFFICIENT_PERMISSION_PREFIX_MSG + "add set to group";
    private static final String PERMISSION_REMOVE_SET_ERROR_MSG = INSUFFICIENT_PERMISSION_PREFIX_MSG + "remove set from group";
    private static final String GROUP_NOT_FOUND_ERROR_MSG = "Group with id: {0} was not found";
    private static final String NEW_ADMIN_NOT_FOUND_IN_CONTRIBUTORS_EXCEPTION_MSG = "New admin id: {0} was not found in group contributors";

    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ObjectIdentifierProvider objectIdentifierProvider;
    private final ContributionRepository contributionRepository;
    private final SetService setService;
    private final TermRepository termRepository;

    public GroupService(UserService userService,
                        UserRepository userRepository,
                        GroupRepository groupRepository,
                        ObjectIdentifierProvider objectIdentifierProvider,
                        ContributionRepository contributionRepository,
                        TermRepository termRepository,
                        SetService setService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.objectIdentifierProvider = objectIdentifierProvider;
        this.contributionRepository = contributionRepository;
        this.termRepository = termRepository;
        this.setService = setService;
    }

    public GroupDTO get(long id) {
        GroupJPAEntity groupEntity = getById(id);

        return GroupDTOMapper.from(groupEntity);
    }

    @Transactional
    public Long create(GroupCreateDTO groupCreateDTO, Principal principal) {
        UserJPAEntity creator = userService.getByEmail(principal.getName());
        String groupName = groupCreateDTO.name();
        String groupIdentifier = objectIdentifierProvider.fromGroupName(groupName);
        List<ContributionDTO> contributions = groupCreateDTO.contributions();

        List<UserJPAEntity> contributors = userRepository.findByIds(extractCreateContributorIds(contributions));
        Set<ContributionJPAEntity> contributionEntities = toContributionJPAEntities(contributions, contributors);
        contributionEntities.add(getCreatorContribution(creator));

        GroupJPAEntity groupEntity = new GroupJPAEntity(groupName, groupIdentifier, contributionEntities, creator);

        groupRepository.persist(groupEntity);

        return groupEntity.getId();
    }

    private Set<Long> extractCreateContributorIds(List<ContributionDTO> contributions) {
        return contributions.stream()
                .map(ContributionDTO::contributor)
                .map(UserDTO::id)
                .collect(Collectors.toSet());
    }

    private List<Long> extractContributorIds(List<ContributionDTO> contributions) {
        return contributions.stream()
                .map(ContributionDTO::contributor)
                .map(UserDTO::id)
                .toList();
    }

    private ContributionJPAEntity getCreatorContribution(UserJPAEntity creator) {
        return new ContributionJPAEntity(PermissionType.READ_WRITE, PermissionType.READ_WRITE, creator);
    }

    private Set<ContributionJPAEntity> toContributionJPAEntities(Collection<ContributionDTO> contributionDTOS, List<UserJPAEntity> contributors) {
        return contributionDTOS.stream()
                .map(contributionDTO -> toContributionJPAEntity(contributors, contributionDTO))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<ContributionJPAEntity> toContributionJPAEntity(List<UserJPAEntity> contributors, ContributionDTO contributionDTO) {
        return getMatchingContributor(contributionDTO, contributors)
                .map(contributor -> new ContributionJPAEntity(PermissionTypeMapper.from(contributionDTO.setPermissionType()), PermissionTypeMapper.from(contributionDTO.groupPermissionType()), contributor));
    }

    private Optional<UserJPAEntity> getMatchingContributor(ContributionDTO contributionDTO, List<UserJPAEntity> contributors) {
        return contributors.stream()
                .filter(contributor -> contributor.getId().equals(contributionDTO.contributor().id()))
                .findFirst();
    }

    public void updateContributions(GroupUpdateDTO groupUpdateDTO, Principal principal) {
        GroupJPAEntity groupEntity = getById(groupUpdateDTO.id());

        UserContributionValidator.validateUserGroupEditPermission(principal, groupEntity, PERMISSION_EDIT_GROUP_ERROR_MSG);

        List<ContributionDTO> contributions = getContributionsWithoutAdmin(groupUpdateDTO, groupEntity);
        Set<ContributionDTO> contributionsToCreate = getContributionsToCreate(contributions);
        Set<ContributionDTO> contributionsToUpdate = getContributionsToUpdate(contributions);

        List<UserJPAEntity> existingContributors = userRepository.findByIds(extractContributorIds(contributions));
        Set<ContributionJPAEntity> contributionEntitiesToCreate = toContributionJPAEntities(contributionsToCreate, existingContributors);

        Set<ContributionJPAEntity> existingContributions = groupEntity.getContributions();
        updateContributions(contributionsToUpdate, existingContributions);

        existingContributions.addAll(contributionEntitiesToCreate);

        groupRepository.persist(groupEntity);
    }

    private List<ContributionDTO> getContributionsWithoutAdmin(GroupUpdateDTO groupUpdateDTO, GroupJPAEntity groupEntity) {
        return groupUpdateDTO.contributions().stream()
                .filter(contribution -> !contribution.contributor().id().equals(groupEntity.getAdmin().getId()))
                .toList();
    }

    private Set<ContributionDTO> getContributionsToCreate(List<ContributionDTO> contributions) {
        return getContributions(contributions, contribution -> Objects.isNull(contribution.id()));
    }

    private Set<ContributionDTO> getContributionsToUpdate(List<ContributionDTO> contributions) {
        return getContributions(contributions, contribution -> !Objects.isNull(contribution.id()));
    }

    private Set<ContributionDTO> getContributions(List<ContributionDTO> contributions, Predicate<ContributionDTO> contributionFilter) {
        return contributions.stream()
                .filter(contributionFilter)
                .collect(Collectors.toSet());
    }

    private void updateContributions(Set<ContributionDTO> contributionsToUpdate, Set<ContributionJPAEntity> existingContributions) {
        Map<Long, ContributionJPAEntity> existingContributionById = existingContributions.stream()
                .collect(Collectors.toMap(ContributionJPAEntity::getId, Function.identity()));

        contributionsToUpdate.stream()
                .filter(contributionToUpdate -> existingContributionById.containsKey(contributionToUpdate.id()))
                .forEach(contributionToUpdate -> rebuildContributionJPAEntity(existingContributionById, contributionToUpdate));
    }

    private void rebuildContributionJPAEntity(Map<Long, ContributionJPAEntity> existingContributionById, ContributionDTO contributionToUpdate) {
        ContributionJPAEntity existingContribution = existingContributionById.get(contributionToUpdate.id());
        PermissionType groupPermissionType = PermissionTypeMapper.from(contributionToUpdate.groupPermissionType());
        PermissionType setPermissionType = PermissionTypeMapper.from(contributionToUpdate.setPermissionType());

        existingContribution.setGroupPermissionType(groupPermissionType);
        existingContribution.setSetPermissionType(setPermissionType);
    }

    private GroupJPAEntity getById(long id) {
        return groupRepository.findByIdOptional(id)
                .orElseThrow(supplyGroupNotFoundException(id));
    }

    private Supplier<MalletNotFoundException> supplyGroupNotFoundException(long groupId) {
        return () -> {
            String message = MessageFormat.format(GROUP_NOT_FOUND_ERROR_MSG, groupId);
            throw new MalletNotFoundException(message);
        };
    }

    public void delete(long id, Principal principal) {
        GroupJPAEntity groupEntity = getById(id);

        UserContributionValidator.validateAdminRole(principal, groupEntity.getAdmin(), PERMISSION_REMOVE_GROUP_ERROR_MSG);

        groupRepository.delete(groupEntity);
    }

    public void updateAdmin(GroupUpdateAdminDTO groupUpdateAdminDTO, Principal principal) {
        GroupJPAEntity groupEntity = getById(groupUpdateAdminDTO.groupId());

        UserContributionValidator.validateAdminRole(principal, groupEntity.getAdmin(), PERMISSION_CHANGE_ADMIN_ERROR_MSG);

        UserJPAEntity newAdminEntity = getNewAdminEntity(groupUpdateAdminDTO, groupEntity);
        groupEntity.setAdmin(newAdminEntity);

        groupRepository.persist(groupEntity);
    }

    private UserJPAEntity getNewAdminEntity(GroupUpdateAdminDTO groupUpdateAdminDTO, GroupJPAEntity groupEntity) {
        return groupEntity.getContributions().stream()
                .map(ContributionJPAEntity::getContributor)
                .filter(contributor -> contributor.getId().equals(groupUpdateAdminDTO.newAdminId()))
                .findFirst()
                .orElseThrow(supplyNewAdminIdNotFoundInContributors(groupUpdateAdminDTO.newAdminId()));
    }


    private Supplier<MalletNotFoundException> supplyNewAdminIdNotFoundInContributors(long newAdminId) {
        String message = MessageFormat.format(NEW_ADMIN_NOT_FOUND_IN_CONTRIBUTORS_EXCEPTION_MSG, newAdminId);
        return () -> new MalletNotFoundException(message);
    }

    public void deleteContributions(GroupContributionDeleteDTO groupContributionDeleteDTO, Principal principal) {
        Long groupId = groupContributionDeleteDTO.groupId();
        GroupJPAEntity groupEntity = getById(groupId);

        UserContributionValidator.validateUserGroupEditPermission(principal, groupEntity, PERMISSION_EDIT_GROUP_ERROR_MSG);

        List<ContributionJPAEntity> contributionsToDelete = contributionRepository.getByIds(groupContributionDeleteDTO.contributionsToDeleteIds());
        Collection<ContributionJPAEntity> groupContributions = groupEntity.getContributions();

        groupContributions.removeAll(contributionsToDelete);

        groupRepository.persist(groupEntity);
    }


    public void addSet(GroupSetDTO groupSetDTO, Principal principal) {
        GroupJPAEntity groupEntity = getById(groupSetDTO.groupId());
        UserContributionValidator.validateUserSetEditPermission(principal, groupEntity, PERMISSION_ADD_SET_ERROR_MSG);

        SetJPAEntity setToClone = setService.getById(groupSetDTO.setId());
        String identifier = objectIdentifierProvider.fromSetName(setToClone.getName());

        SetJPAEntity clonedSet = new SetJPAEntity(setToClone, identifier);
        groupEntity.addSet(clonedSet);


        groupRepository.persist(groupEntity);
    }

    public void removeSet(GroupSetDTO groupSetDTO, Principal principal) {
        GroupJPAEntity groupEntity = getById(groupSetDTO.groupId());

        UserContributionValidator.validateUserSetEditPermission(principal, groupEntity, PERMISSION_REMOVE_SET_ERROR_MSG);

        SetJPAEntity setToRemove = setService.getById(groupSetDTO.setId());
        groupEntity.removeSet(setToRemove);

        groupRepository.persist(groupEntity);
    }

    @Transactional
    public void createSet(GroupSetCreateDTO groupSetCreateDTO, Principal principal) {
        GroupJPAEntity groupEntity = getById(groupSetCreateDTO.groupId());

        UserContributionValidator.validateUserSetEditPermission(principal, groupEntity, PERMISSION_ADD_SET_ERROR_MSG);

        SetCreateDTO set = groupSetCreateDTO.set();
        Set<TermJPAEntity> mergedTerms = getToCreateAndExistingTerms(set);
        String setTopic = set.topic();
        String setIdentifier = objectIdentifierProvider.fromSetName(setTopic);

        SetJPAEntity setJPAEntity = new SetJPAEntity(setTopic, setIdentifier, set.description(), mergedTerms);

        groupEntity.addSet(setJPAEntity);

        groupRepository.persist(groupEntity);
    }

    private Set<TermJPAEntity> getToCreateAndExistingTerms(SetCreateDTO setCreateDTO) {
        List<TermJPAEntity> existingTerms = termRepository.findByIds(setCreateDTO.existingTermIds());
        List<TermJPAEntity> termsToCreate = getTermsToCreate(setCreateDTO);

        return Stream.concat(termsToCreate.stream(), existingTerms.stream())
                .collect(Collectors.toSet());
    }

    private List<TermJPAEntity> getTermsToCreate(SetCreateDTO setCreateDTO) {
        return setCreateDTO.termsToCreate().stream()
                .map(this::toTermJPAEntity)
                .toList();
    }

    private TermJPAEntity toTermJPAEntity(TermCreateDTO term) {
        return new TermJPAEntity(
                term.term(),
                Language.from(term.language().name()),
                term.definition(),
                toTranslationTermJPAEntity(term)
        );
    }

    private TermJPAEntity toTranslationTermJPAEntity(TermCreateDTO term) {
        return new TermJPAEntity(
                term.translation().term(),
                Language.from(term.translation().language().name()),
                term.translation().definition()
        );
    }

}
