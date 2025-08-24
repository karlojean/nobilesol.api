package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Investor;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InvestorRepository extends JpaRepository<Investor, UUID> {
    @Query("""
    SELECT i
    FROM Investor i
    WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :term, '%'))
       OR LOWER(i.tradeName) LIKE LOWER(CONCAT('%', :term, '%'))
       OR LOWER(i.companyName) LIKE LOWER(CONCAT('%', :term, '%'))
    """)
    Page<Investor> search(@Param("term") String term, Pageable pageable);

    Optional<Investor> findByAccountId(UUID accountId);

    boolean existsByDocumentNumber(String documentNumber);
}
