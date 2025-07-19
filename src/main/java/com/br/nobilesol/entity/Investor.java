package com.br.nobilesol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "investors")
public class Investor {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Size(max = 255)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Size(max = 14)
    @NotNull
    @Column(name = "document_number", nullable = false, length = 14)
    private String documentNumber;

    @Size(max = 11)
    @Column(name = "phone_number", length = 11)
    private String phoneNumber;
}