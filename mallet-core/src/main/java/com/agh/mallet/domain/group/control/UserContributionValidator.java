package com.agh.mallet.domain.group.control;

import com.agh.mallet.domain.group.entity.ContributionJPAEntity;
import com.agh.mallet.domain.group.entity.GroupJPAEntity;
import com.agh.mallet.domain.group.entity.PermissionType;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import com.agh.mallet.infrastructure.exception.MalletForbiddenException;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.function.Supplier;

public class UserContributionValidator {

    private UserContributionValidator() {}

    private static final String USER_NOT_FOUND_IN_CONTRIBUTORS_EXCEPTION_MSG = "User was not found in contributions of group with id {0}";

    public static void validateUserSetEditPermission(Principal principal,
                                                     GroupJPAEntity groupEntity,
                                                     String validationErrorMessage) {
        UserJPAEntity groupAdmin = groupEntity.getAdmin();
        ContributionJPAEntity requesterContribution = getContribution(principal.getName(), groupEntity);

        if (!groupAdmin.getEmail().equals(principal.getName()) || !PermissionType.READ_WRITE.equals(requesterContribution.getSetPermissionType())) {
            throw new MalletForbiddenException(validationErrorMessage);
        }
    }

    public static void validateUserGroupEditPermission(Principal principal,
                                                       GroupJPAEntity groupEntity,
                                                       String validationErrorMessage) {
        UserJPAEntity groupAdmin = groupEntity.getAdmin();
        ContributionJPAEntity requesterContribution = getContribution(principal.getName(), groupEntity);

        if (!groupAdmin.getEmail().equals(principal.getName()) || !PermissionType.READ_WRITE.equals(requesterContribution.getGroupPermissionType())) {
            throw new MalletForbiddenException(validationErrorMessage);
        }
    }

    private static ContributionJPAEntity getContribution(String userEmail, GroupJPAEntity groupEntity) {
        return groupEntity.getContributions().stream()
                .filter(contribution -> contribution.getContributor().getEmail().equals(userEmail))
                .findAny()
                .orElseThrow(supplyUserNotInContributorsOfGroupException(groupEntity.getId()));
    }

    private static Supplier<MalletForbiddenException> supplyUserNotInContributorsOfGroupException(Long groupId) {
        return () -> {
            String message = MessageFormat.format(USER_NOT_FOUND_IN_CONTRIBUTORS_EXCEPTION_MSG, groupId);
            return new MalletForbiddenException(message);
        };
    }

    public static void validateAdminRole(Principal principal,
                                         UserJPAEntity groupAdmin,
                                         String errorMessage) {
        if (!groupAdmin.getEmail().equals(principal.getName())) {
            throw new MalletForbiddenException(errorMessage);
        }
    }

}
