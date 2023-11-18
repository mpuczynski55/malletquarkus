package com.agh.mallet.domain.set.control.service;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetDetailDTO;
import com.agh.api.SetUpdateDTO;
import com.agh.api.TermCreateDTO;
import com.agh.api.TermDTO;
import com.agh.mallet.domain.group.control.GroupRepository;
import com.agh.mallet.domain.group.control.UserContributionValidator;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;
import com.agh.mallet.domain.set.control.repository.SetRepository;
import com.agh.mallet.domain.set.entity.SetJPAEntity;
import com.agh.mallet.domain.term.entity.Language;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import com.agh.mallet.infrastructure.exception.MalletForbiddenException;
import com.agh.mallet.infrastructure.exception.MalletNotFoundException;
import com.agh.mallet.infrastructure.mapper.SetBasicsDTOMapper;
import com.agh.mallet.infrastructure.mapper.SetDetailDTOMapper;
import com.agh.mallet.infrastructure.utils.NextChunkRebuilder;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.Dependent;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Dependent
public class SetService {

    public static final String SET_NOT_FOUND_ERROR_MSG = "Set with id: {0} was not found";
    private static final String GROUP_NOT_FOUND_ERROR_MSG = "Group with id: {0} was not found";

    private static final String INSUFFICIENT_PERMISSION_PREFIX_MSG = "Insufficient permission to ";
    private static final String PERMISSION_SYNC_UPDATE_ERROR_MSG = INSUFFICIENT_PERMISSION_PREFIX_MSG + "update set";
    private final SetRepository setRepository;
    private final GroupRepository groupRepository;
    private final NextChunkRebuilder nextChunkRebuilder;

    public SetService(SetRepository setRepository, GroupRepository groupRepository, NextChunkRebuilder nextChunkRebuilder) {
        this.setRepository = setRepository;
        this.groupRepository = groupRepository;
        this.nextChunkRebuilder = nextChunkRebuilder;
    }

    public SetJPAEntity getById(long id) {
        return setRepository.findByIdOptional(id)
                .orElseThrow(supplySetNotFoundException(id));
    }

    private Supplier<MalletNotFoundException> supplySetNotFoundException(long setId) {
        String message = MessageFormat.format(SET_NOT_FOUND_ERROR_MSG, setId);
        return () -> new MalletNotFoundException(message);
    }

    public SetBasicDTO getBasics(Set<Long> ids) {
        List<SetJPAEntity> sets = setRepository.findByIds(ids);

        return SetBasicsDTOMapper.from(sets);
    }

    public SetBasicDTO getBasics(String topic) {
        List<SetJPAEntity> sets = setRepository.findAllByNameContaining(topic);

        return SetBasicsDTOMapper.from(sets);
    }

    public SetDetailDTO get(long setId,
                            int startPosition,
                            int limit,
                            String primaryLanguage) {
        if (limit > 30) {
            limit = 30;
        }
        Page page = Page.of(startPosition, startPosition + limit);
        Language language = Language.from(primaryLanguage);

        List<TermJPAEntity> terms = setRepository.findAllTermsBySetIdAndLanguage(setId, language, page);

        String nextChunkUri = nextChunkRebuilder.rebuild(terms, startPosition, limit);

        return SetDetailDTOMapper.from(setId, terms, nextChunkUri);
    }

    public SetBasicDTO getBasics(Set<Long> ids,
                                 String topic,
                                 int startPosition,
                                 int limit,
                                 String primaryLanguage) {
        if (Objects.nonNull(ids)) {
            return getBasics(ids);
        }

        if (Objects.nonNull(topic)) {
            return getBasics(topic);
        }

        if (limit > 10) {
            limit = 10;
        }

        Page page = Page.of(startPosition, startPosition + limit);
        Language language = Language.from(primaryLanguage);

        List<SetJPAEntity> sets = setRepository.findAllByTermsLanguage(language, page);

        String nextChunkUri = nextChunkRebuilder.rebuild(sets, startPosition, limit);

        return SetBasicsDTOMapper.from(sets, nextChunkUri);
    }
    public void syncSet(SetUpdateDTO setUpdateDTO, Principal principal) {
        SetJPAEntity setEntity = getById(setUpdateDTO.id());

        validateUserPermissionsToUpdateSet(principal, setEntity, setUpdateDTO);

        setEntity.setDescription(setUpdateDTO.description());
        setEntity.setName(setUpdateDTO.topic());

        List<TermJPAEntity> termsToCreate = setUpdateDTO.termsToCreate().stream()
                .map(this::toTermJPAEntity)
                .toList();

        Map<Long, TermDTO> termById = setUpdateDTO.termsToUpdate().stream()
                .collect(Collectors.toMap(TermDTO::id, Function.identity()));

        Set<TermJPAEntity> terms = setEntity.getTerms();
        terms.forEach(term -> updateTerms(termById, term));
        terms.addAll(termsToCreate);

        setRepository.persist(setEntity);
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

    private void updateTerms(Map<Long, TermDTO> termById, TermJPAEntity term) {
        if (!termById.containsKey(term.getId())) {
            return;
        }

        TermDTO newTerm = termById.get(term.getId());
        term.setTerm(newTerm.term());
        term.setDefinition(newTerm.definition());
        TermJPAEntity translationEntity = term.getTranslation();
        TermDTO newTranslation = newTerm.translation();
        translationEntity.setDefinition(newTranslation.definition());
        translationEntity.setTerm(newTranslation.term());
    }

    private void validateUserPermissionsToUpdateSet(Principal principal,
                                                    SetJPAEntity setJPAEntity,
                                                    SetUpdateDTO setUpdateDTO) {
        if (Objects.isNull(setUpdateDTO.groupId()) && !setJPAEntity.getCreator().getEmail().equals(principal.getName())) {
            throw new MalletForbiddenException(PERMISSION_SYNC_UPDATE_ERROR_MSG);
        }

        Long groupId = setUpdateDTO.groupId();
        GroupJPAEntity groupEntity = groupRepository.findByIdOptional(groupId)
                .orElseThrow(supplyGroupNotFoundException(groupId));
        UserContributionValidator.validateUserSetEditPermission(principal, groupEntity, PERMISSION_SYNC_UPDATE_ERROR_MSG);
    }

    private Supplier<MalletNotFoundException> supplyGroupNotFoundException(long groupId) {
        return () -> {
            String message = MessageFormat.format(GROUP_NOT_FOUND_ERROR_MSG, groupId);
            throw new MalletNotFoundException(message);
        };
    }

}