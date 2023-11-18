package com.agh.mallet.domain.user.user.control.service;

import com.agh.api.UserDTO;
import com.agh.api.UserDetailDTO;
import com.agh.api.UserLogInDTO;
import com.agh.api.UserRegistrationDTO;
import com.agh.mallet.configuration.PasswordEncoder;
import com.agh.mallet.domain.user.user.control.exception.MalletUserException;
import com.agh.mallet.domain.user.user.control.repository.UserRepository;
import com.agh.mallet.domain.user.user.control.utils.EmailTemplateProvider;
import com.agh.mallet.domain.user.user.control.utils.UserValidator;
import com.agh.mallet.domain.user.user.entity.ConfirmationTokenJPAEntity;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import com.agh.mallet.infrastructure.mapper.UserDTOMapper;
import com.agh.mallet.infrastructure.mapper.UserInformationDTOMapper;
import com.agh.mallet.infrastructure.utils.ObjectIdentifierProvider;
import jakarta.enterprise.context.Dependent;
import org.jboss.resteasy.reactive.RestResponse;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Dependent
public class UserService {

    private static final String USER_ALREADY_EXISTS_ERROR_MSG = "User with provided email: {0} already exists";
    private static final String USER_NOT_FOUND_ERROR_MSG = "User with provided email: {0} does not exist";

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectIdentifierProvider objectIdentifierProvider;

    public UserService(UserRepository userRepository, UserValidator userValidator, ConfirmationTokenService confirmationTokenService, EmailService emailService, PasswordEncoder passwordEncoder, ObjectIdentifierProvider objectIdentifierProvider) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.objectIdentifierProvider = objectIdentifierProvider;
    }

    public void signUp(UserRegistrationDTO userInfo) {
        String email = userInfo.email();
        userValidator.validateEmail(email);

        Optional<UserJPAEntity> userJPAEntity = userRepository.findByEmail(email);
        userJPAEntity.ifPresent(throwUserAlreadyExistsException());

        UserJPAEntity user = mapToUserEntity(userInfo);
        save(user);

        ConfirmationTokenJPAEntity confirmationToken = confirmationTokenService.save(user);

        //todo confirmation link
        emailService.sendMail("Mallet account confirmation", email, EmailTemplateProvider.getEmailConfirmationTemplate(""));
    }

    private UserJPAEntity mapToUserEntity(UserRegistrationDTO userInfo) {
        String encodedPassword = passwordEncoder.hash(userInfo.password());
        String username = userInfo.username();
        LocalDate registrationDate = LocalDate.now();
        String email = userInfo.email();
        String identifier = objectIdentifierProvider.fromUsername(username);

        return new UserJPAEntity(username, encodedPassword, registrationDate, email, identifier);
    }

    private Consumer<UserJPAEntity> throwUserAlreadyExistsException() {
        return user -> {
            String errorMsg = MessageFormat.format(USER_ALREADY_EXISTS_ERROR_MSG, user.getEmail());
            throw new MalletUserException(errorMsg, RestResponse.Status.FORBIDDEN);
        };
    }

    public UserDetailDTO logIn(UserLogInDTO userInfo) {
        String email = userInfo.email();
        userValidator.validateEmail(email);

        UserJPAEntity userEntity = getByEmail(email);

        userValidator.validateUserLogIn(userEntity, userInfo.password());

        return UserInformationDTOMapper.from(userEntity);
    }

    private Supplier<RuntimeException> throwUserNotFoundException(String email) {
        return () -> {
            String errorMsg = MessageFormat.format(USER_NOT_FOUND_ERROR_MSG, email);
            throw new MalletUserException(errorMsg, RestResponse.Status.NOT_FOUND);
        };
    }

    public UserJPAEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(throwUserNotFoundException(email));
    }

    public List<UserDTO> get(String username) {
        if (username.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserJPAEntity> users = userRepository.findAllByUsernameContainingIgnoreCase(username);

        return UserDTOMapper.from(users);
    }

    public void save(UserJPAEntity userJPAEntity){
         userRepository.persist(userJPAEntity);
    }
}
