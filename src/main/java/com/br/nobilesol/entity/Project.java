package com.br.nobilesol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project extends Auditable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @NotNull
    @Column(name = "rated_power_w", nullable = false)
    private Long ratedPowerW;

    @NotNull
    @Column(name = "peak_power_wp", nullable = false)
    private Long peakPowerWp;

    @Size(max = 20)
    @NotNull
    @Column(name = "business_model", nullable = false, length = 20)
    private String businessModel;

    @Size(max = 20)
    @Column(name = "energy_distribution_rule", length = 20)
    private String energyDistributionRule;

    @Size(max = 255)
    @Column(name = "utility_company")
    private String utilityCompany;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'planning'")
    @Column(name = "project_status", nullable = false, length = 20)
    private String projectStatus;

    @Column(name = "construction_start_date")
    private LocalDate constructionStartDate;

    @Column(name = "commercial_operation_date")
    private LocalDate commercialOperationDate;

    @Column(name = "expected_annual_generation_wh")
    private Long expectedAnnualGenerationWh;

    @OneToMany(mappedBy = "project")
    private Set<Plant> plants = new LinkedHashSet<>();

}