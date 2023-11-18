package com.agh.mallet.domain.term.control.service;

import com.agh.api.TermBasicListDTO;
import com.agh.mallet.domain.term.control.repository.TermRepository;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import com.agh.mallet.infrastructure.mapper.TermBasicListDTOMapper;
import com.agh.mallet.infrastructure.utils.NextChunkRebuilder;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.Dependent;

import java.util.List;

@Dependent
public class TermService {

    private final TermRepository termRepository;
    private final NextChunkRebuilder nextChunkRebuilder;

    public TermService(TermRepository termRepository, NextChunkRebuilder nextChunkRebuilder) {
        this.termRepository = termRepository;
        this.nextChunkRebuilder = nextChunkRebuilder;
    }

    public TermBasicListDTO getByTerm(String term,
                                      int startPosition,
                                      int limit) {
        Page page = Page.of(startPosition, startPosition + limit);

        List<TermJPAEntity> terms = termRepository.findAllByTerm(term, page);

        String nextChunkUri = nextChunkRebuilder.rebuild(terms, startPosition, limit);

        return TermBasicListDTOMapper.from(terms, nextChunkUri);
    }

}
