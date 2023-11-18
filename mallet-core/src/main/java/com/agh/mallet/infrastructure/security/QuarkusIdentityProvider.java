package com.agh.mallet.infrastructure.security;

import com.agh.mallet.domain.user.user.control.exception.MalletUserException;
import com.agh.mallet.domain.user.user.control.utils.UserValidator;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.jpa.runtime.JpaIdentityProvider;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@ApplicationScoped
public class QuarkusIdentityProvider extends JpaIdentityProvider {

    private static final String USER_QUERY = "SELECT u FROM UserJPAEntity u WHERE u.email = :email";

    private final UserValidator userValidator;

    public QuarkusIdentityProvider(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public SecurityIdentity authenticate(EntityManager em, UsernamePasswordAuthenticationRequest request) {
        String email = request.getUsername();
        char[] rawPassword = request.getPassword().getPassword();

        List<UserJPAEntity> users = em.createQuery(USER_QUERY, UserJPAEntity.class)
                .setParameter("email", email)
                .getResultList();

        if(users.isEmpty()){
            throw new MalletUserException("User with email: " + email + " was not found", RestResponse.Status.NOT_FOUND);
        }

        UserJPAEntity user = users.get(0);
        userValidator.validateUserLogIn(user, String.valueOf(rawPassword));

        return QuarkusSecurityIdentity.builder()
                .setPrincipal(new QuarkusPrincipal(email))
                .build();
    }

}
