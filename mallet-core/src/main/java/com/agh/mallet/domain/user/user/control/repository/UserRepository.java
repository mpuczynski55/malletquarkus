package com.agh.mallet.domain.user.user.control.repository;

import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserJPAEntity> {

    public Optional<UserJPAEntity> findByEmail(String email){
        return find("email", email)
                .firstResultOptional();
    }

    public List<UserJPAEntity> findByIds(Collection<Long> ids){
        return list("id in ?1", ids);
    }

    public  long countAllByUsername(String username){
        return count("username", username);
    }

    public  List<UserJPAEntity> findAllByUsernameContainingIgnoreCase(String username){
        return list("username like ?1", "%" + username + "%");
    }

}