package com.agh.mallet.domain.group.control;

import com.agh.mallet.domain.group.entity.ContributionJPAEntity;
import com.agh.mallet.infrastructure.exception.MalletNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@ApplicationScoped
public class ContributionRepository implements PanacheRepository<ContributionJPAEntity> {

    private static final String CONTRIBUTION_NOT_FOUND_ERROR_MSG = "Contribution with id: {0} was not found";

    public ContributionJPAEntity getById(Long id) {
        return findByIdOptional(id)
                .orElseThrow(supplyContributionNotFoundException(id));
    }

    public List<ContributionJPAEntity> getByIds(Collection<Long> ids) {
        return list("id in ?1", ids);
    }


    private Supplier<MalletNotFoundException> supplyContributionNotFoundException(Long id) {
        return () -> {
            String message = MessageFormat.format(CONTRIBUTION_NOT_FOUND_ERROR_MSG, id);
            return new MalletNotFoundException(message);
        };
    }

}
