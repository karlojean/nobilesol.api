package com.br.nobilesol.entity;

import com.br.nobilesol.entity.enums.InvestorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "investors",
        uniqueConstraints = @UniqueConstraint(name="uq_investors_document", columnNames = "document_number"),
        indexes = @Index(name="idx_investors_account_id", columnList = "account_id"))
public class Investor extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private InvestorType type;

    @Column(name = "document_number", nullable = false, length = 20)
    private String documentNumber;

    // INDIVIDUAL
    @Column(name = "name", length = 255)
    private String name;

    // COMPANY
    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "trade_name", length = 255)
    private String tradeName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    public String getDisplayName() {
        return (type == InvestorType.INDIVIDUAL)
                ? name
                : (tradeName != null && !tradeName.isBlank() ? tradeName : companyName);
    }
}
