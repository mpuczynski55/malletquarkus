package com.agh.mallet.domain.group.entity;

import com.agh.mallet.domain.set.entity.SetJPAEntity;
import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APP_GROUP")
public class GroupJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IDENTIFIER", nullable = false)
    private String identifier;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "GROUP_ID")
    private Set<ContributionJPAEntity> contributions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "GROUP_ID")
    private Set<SetJPAEntity> sets = new HashSet<>();

    @ManyToOne
    private UserJPAEntity admin;

    public GroupJPAEntity() {
    }

    public GroupJPAEntity(String name, String identifier, Set<ContributionJPAEntity> contributions, UserJPAEntity admin) {
        this.name = name;
        this.identifier = identifier;
        this.contributions = contributions;
        this.admin = admin;
    }

    public GroupJPAEntity(Long id, String name, String identifier, Set<ContributionJPAEntity> contributions, Set<SetJPAEntity> sets, UserJPAEntity admin) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.contributions = contributions;
        this.sets = sets;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<ContributionJPAEntity> getContributions() {
        return contributions;
    }

    public void setContributions(Set<ContributionJPAEntity> contributions) {
        this.contributions = contributions;
    }

    public Set<SetJPAEntity> getSets() {
        return sets;
    }

    public void setSets(Set<SetJPAEntity> sets) {
        this.sets = sets;
    }

    public UserJPAEntity getAdmin() {
        return admin;
    }

    public void setAdmin(UserJPAEntity admin) {
        this.admin = admin;
    }

    public GroupJPAEntity addSet(SetJPAEntity groupSet) {
        sets.add(groupSet);
        return this;
    }

    public UserJPAEntity removeSet(SetJPAEntity groupSet) {
        sets.remove(groupSet);
        return this.getAdmin();
    }

}
