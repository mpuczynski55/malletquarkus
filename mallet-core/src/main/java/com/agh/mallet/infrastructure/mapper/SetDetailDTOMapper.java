package com.agh.mallet.infrastructure.mapper;

import com.agh.api.SetDetailDTO;
import com.agh.api.TermDTO;
import com.agh.mallet.domain.set.entity.SetJPAEntity;
import com.agh.mallet.domain.term.entity.TermJPAEntity;

import java.util.Collection;
import java.util.List;

public class SetDetailDTOMapper {

    private SetDetailDTOMapper() {}

    public static SetDetailDTO from(long setId,
                                    List<TermJPAEntity> termJPAEntities,
                                    String nextChunkUri) {
        List<TermDTO> terms = TermDTOMapper.from(termJPAEntities);

        return SetDetailDTO.builder()
                .id(setId)
                .terms(terms)
                .nextChunkUri(nextChunkUri)
                .build();
    }

    public static List<SetDetailDTO> from(Collection<SetJPAEntity> sets,
                                          String nextChunkUri) {
        return sets.stream()
                .map(entity -> from(entity, nextChunkUri))
                .toList();
    }

    public static SetDetailDTO from(SetJPAEntity set,
                                    String nextChunkUri) {
        List<TermDTO> terms = TermDTOMapper.from(set.getTerms());

        return SetDetailDTO.builder()
                .id(set.getId())
                .name(set.getName())
                .description(set.getDescription())
                .terms(terms)
                .nextChunkUri(nextChunkUri)
                .build();
    }

}
