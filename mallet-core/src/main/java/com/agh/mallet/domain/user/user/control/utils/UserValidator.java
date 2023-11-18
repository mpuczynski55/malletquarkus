package com.agh.mallet.domain.user.user.control.utils;

import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import com.agh.mallet.configuration.PasswordEncoder;
import com.agh.mallet.domain.user.user.control.exception.MalletUserException;
import jakarta.enterprise.context.Dependent;
import org.jboss.resteasy.reactive.RestResponse;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Dependent
public class UserValidator {

    private static final String INVALID_EMAIL_ERROR_MSG = "Provided email: {0} is not valid";
    private static final String USER_NOT_ENABLED_ERROR_MSG = "User with email: {0} is not enabled";
    private static final String INCORRECT_PASSWORD_ERROR_MSG = "Provided password is incorrect";

    private final PasswordEncoder passwordEncoder;

    public UserValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void validateUserLogIn(UserJPAEntity user,
                                  String providedPassword) {
        validateActiveness(user);
        validatePassword(user, providedPassword);
    }

    private void validatePassword(UserJPAEntity user, String providedPassword) {
        if (!passwordEncoder.matches(providedPassword, user.getPassword())) {
            throw new MalletUserException(INCORRECT_PASSWORD_ERROR_MSG, RestResponse.Status.PRECONDITION_FAILED);
        }
    }

    public void validateActiveness(UserJPAEntity user) {
        if (Boolean.FALSE.equals(user.getEnabled())) {
            String exceptionMessage = MessageFormat.format(USER_NOT_ENABLED_ERROR_MSG, user.getEmail());
            throw new MalletUserException(exceptionMessage, RestResponse.Status.FORBIDDEN);
        }
    }

    public void validateEmail(String email) {
        if (!isEmailValid(email)) {
            String exceptionMessage = MessageFormat.format(INVALID_EMAIL_ERROR_MSG, email);
            throw new MalletUserException(exceptionMessage, RestResponse.Status.PRECONDITION_FAILED);
        }
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
