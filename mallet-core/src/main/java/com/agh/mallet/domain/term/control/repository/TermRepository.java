package com.agh.mallet.domain.term.control.repository;

import com.agh.mallet.domain.term.entity.TermJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.List;


@ApplicationScoped
public class TermRepository implements PanacheRepository<TermJPAEntity> {

    public List<TermJPAEntity> findAllByTerm(String term, Page page) {
        return find("term", term)
                .page(page)
                .list();
    }

    public List<TermJPAEntity> findByIds(Collection<Long> ids) {
        return list("id in ?1", ids);
    }
}
