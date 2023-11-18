package com.agh.mallet.infrastructure.mapper;

import com.agh.api.TermDTO;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import com.agh.mallet.infrastructure.utils.LanguageConverter;

import java.util.Collection;
import java.util.List;

public class TermDTOMapper {

    private TermDTOMapper() {
    }

    public static List<TermDTO> from(Collection<TermJPAEntity> entities) {
        return entities.stream()
                .map(TermDTOMapper::from)
                .toList();
    }

    private static TermDTO fromTranslation(TermJPAEntity entity) {
        return TermDTO.builder()
                .id(entity.getId())
                .term(entity.getTerm())
                .definition(entity.getDefinition())
                .language(LanguageConverter.from(entity.getLanguage()))
                .build();
    }


    private static TermDTO from(TermJPAEntity entity) {
        TermDTO translation =  TermDTOMapper.fromTranslation(entity.getTranslation());
        TermDTO.TermDTOBuilder builder = TermDTO.builder();
        return builder
                .id(entity.getId())
                .term(entity.getTerm())
                .definition(entity.getDefinition())
                .language(LanguageConverter.from(entity.getLanguage()))
                .translation(translation)
                .build();
    }
}

