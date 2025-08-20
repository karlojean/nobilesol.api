package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Investor;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvestorRepository extends JpaRepository<Investor, UUID> {
    Page<Investor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Investor> findByAccountId(UUID accountId);

    boolean existsByDocumentNumber(String documentNumber);
}
