package com.agh.mallet.domain.user.user.entity;

import com.agh.mallet.domain.group.entity.GroupJPAEntity;
import com.agh.mallet.domain.set.entity.SetJPAEntity;
import com.agh.mallet.domain.term.entity.TermJPAEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER_APP")
public class UserJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "REGISTRATION_DATE", nullable = false)
    private LocalDate registrationDate;
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "ENABLED")
    private Boolean enabled = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_KNOWN_TERMS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "KNOWN_TERM_ID"))
    private Set<TermJPAEntity> knownTerms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_GROUPS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private List<GroupJPAEntity> userGroups = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "creator")
    private List<SetJPAEntity> userSets = new ArrayList<>();


    public UserJPAEntity() {}

    public UserJPAEntity(String username, String password, LocalDate registrationDate, String email, String identifier) {
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
        this.email = email;
        this.identifier = identifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<TermJPAEntity> getKnownTerms() {
        return knownTerms;
    }

    public void setKnownTerms(Set<TermJPAEntity> knownTerms) {
        this.knownTerms = knownTerms;
    }

    public List<GroupJPAEntity> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<GroupJPAEntity> userGroups) {
        this.userGroups = userGroups;
    }

    public void addUserGroup(GroupJPAEntity userGroups) {
        this.userGroups.add(userGroups);
    }

    public List<SetJPAEntity> getUserSets() {
        return userSets;
    }

    public void setUserSets(List<SetJPAEntity> userSets) {
        this.userSets = userSets;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void addUserSet(SetJPAEntity userSet) {
        userSets.add(userSet);
        userSet.setCreator(this);
    }

    public void removeUserSet(SetJPAEntity userSet) {
        userSets.remove(userSet);
        userSet.setCreator(null);
    }

}

