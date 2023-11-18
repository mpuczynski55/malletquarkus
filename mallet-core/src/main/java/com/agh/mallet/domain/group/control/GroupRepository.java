package com.agh.mallet.domain.group.control;

import com.agh.mallet.domain.group.entity.GroupJPAEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GroupRepository implements PanacheRepository<GroupJPAEntity> {

    public long countAllByName(String name){
        return count("name", name);
    }

}
