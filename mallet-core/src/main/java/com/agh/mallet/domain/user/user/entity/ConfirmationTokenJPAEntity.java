package com.agh.mallet.domain.user.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONFIRMATION_TOKEN")
public class ConfirmationTokenJPAEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(name = "EXPIRES_AT")

    private LocalDateTime expiresAt;
    @Column(name = "CONFIRMED_AT")

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserJPAEntity user;

    public ConfirmationTokenJPAEntity() {}

    public ConfirmationTokenJPAEntity(String token, LocalDateTime createdAt, LocalDateTime expiresAt, UserJPAEntity user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public UserJPAEntity getUser() {
        return user;
    }

}
