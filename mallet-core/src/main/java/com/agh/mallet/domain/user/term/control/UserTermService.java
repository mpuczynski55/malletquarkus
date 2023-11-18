package com.agh.mallet.domain.user.term.control;


import com.agh.mallet.domain.term.control.repository.TermRepository;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import com.agh.mallet.domain.user.user.control.service.UserService;
import com.agh.mallet.domain.user.user.control.utils.UserValidator;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import jakarta.enterprise.context.Dependent;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Dependent
public class UserTermService {

    private final UserService userService;
    private final TermRepository termRepository;
    private final UserValidator userValidator;

    public UserTermService(UserService userService, TermRepository termRepository, UserValidator userValidator) {
        this.userService = userService;
        this.termRepository = termRepository;
        this.userValidator = userValidator;
    }

    public void updateKnown(Set<Long> userKnownTermIds, Principal principal){
        UserJPAEntity userEntity = userService.getByEmail(principal.getName());
        userValidator.validateActiveness(userEntity);

        List<TermJPAEntity> termsToUpdate = termRepository.findByIds(userKnownTermIds);

        userEntity.getKnownTerms().addAll(termsToUpdate);

        userService.save(userEntity);
    }

}
