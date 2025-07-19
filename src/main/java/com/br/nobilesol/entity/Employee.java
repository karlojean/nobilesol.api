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
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Size(max = 100)
    @Column(name = "department", length = 100)
    private String department;
}