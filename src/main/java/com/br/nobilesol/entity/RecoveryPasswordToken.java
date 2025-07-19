package com.br.nobilesol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "recovery_password_token")
public class RecoveryPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @Column(name = "token", nullable = false, length = Integer.MAX_VALUE)
    private String token;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
