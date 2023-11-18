package com.agh.mallet.domain.user.user.control.repository;

import com.agh.mallet.domain.user.user.entity.ConfirmationTokenJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ConfirmationTokenRepository implements PanacheRepository<ConfirmationTokenJPAEntity> {

    public Optional<ConfirmationTokenJPAEntity> findByToken(String token){
        return find("token", token)
                .singleResultOptional();
    }
}
