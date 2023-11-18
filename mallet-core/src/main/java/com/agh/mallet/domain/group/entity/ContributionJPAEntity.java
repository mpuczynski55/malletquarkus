package com.agh.mallet.domain.group.entity;

import com.agh.mallet.domain.user.user.entity.UserJPAEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTRIBUTION")
public class ContributionJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "SET_PERMISSION_TYPE")
    private PermissionType setPermissionType = PermissionType.READ;

    @Enumerated(EnumType.STRING)
    @Column(name = "GROUP_PERMISSION_TYPE")
    private PermissionType groupPermissionType = PermissionType.READ;

    @ManyToOne
    private UserJPAEntity contributor;

    protected ContributionJPAEntity() {}

    public ContributionJPAEntity(PermissionType setPermissionType, PermissionType groupPermissionType, UserJPAEntity contributor) {
        this.setPermissionType = setPermissionType;
        this.groupPermissionType = groupPermissionType;
        this.contributor = contributor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionType getSetPermissionType() {
        return setPermissionType;
    }

    public void setSetPermissionType(PermissionType setPermissionType) {
        this.setPermissionType = setPermissionType;
    }

    public PermissionType getGroupPermissionType() {
        return groupPermissionType;
    }

    public void setGroupPermissionType(PermissionType groupPermissionType) {
        this.groupPermissionType = groupPermissionType;
    }

    public UserJPAEntity getContributor() {
        return contributor;
    }

    public void setContributor(UserJPAEntity contributor) {
        this.contributor = contributor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ContributionJPAEntity other))
            return false;

        return id != null && id.equals(other.getId()) &&
                setPermissionType != null && setPermissionType.equals(other.getSetPermissionType()) &&
                groupPermissionType != null && groupPermissionType.equals(other.getGroupPermissionType()) &&
                contributor != null && contributor.equals(other.getContributor());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
