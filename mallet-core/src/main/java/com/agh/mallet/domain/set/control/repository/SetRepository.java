package com.agh.mallet.domain.set.control.repository;

import com.agh.mallet.domain.set.entity.SetJPAEntity;
import com.agh.mallet.domain.term.entity.Language;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import com.agh.mallet.infrastructure.exception.MalletNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@ApplicationScoped
public class SetRepository implements PanacheRepository<SetJPAEntity> {

    private static final String SET_NOT_FOUND_ERROR_MSG = "Set with id: {0} was not found";

    public List<TermJPAEntity> findAllTermsBySetIdAndLanguage(long setId, Language language, Page page) {
        return find("id = ?1 and terms.language = ?2", setId, language)
                .page(page)
                .list()
                .stream()
                .map(SetJPAEntity::getTerms)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<SetJPAEntity> findAllByTermsLanguage(Language language, Page page){
        return find("terms.language = ?2", language)
                .page(page)
                .list();
    }

    public List<SetJPAEntity> findAllByNameContaining(String name){
        return find("select u1_0 from set u1_0 where upper(u1_0.username) like upper(?) escape '\\'", name)
                .list();
    }

    public List<SetJPAEntity> findByIds(Collection<Long> ids){
        return find("id in ?1", ids)
                .list();
    }

    public SetJPAEntity getById(long id) {
        return findByIdOptional(id)
                .orElseThrow(supplySetNotFoundException(id));
    }

    private static Supplier<MalletNotFoundException> supplySetNotFoundException(long setId) {
        String message = MessageFormat.format(SET_NOT_FOUND_ERROR_MSG, setId);
        return () -> new MalletNotFoundException(message);
    }


    public long countAllByName(String setName){
       return count("name", setName);
    }
}
