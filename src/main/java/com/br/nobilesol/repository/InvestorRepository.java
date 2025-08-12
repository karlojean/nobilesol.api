package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Investor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InvestorRepository extends JpaRepository<Investor, UUID> {
    @Query("SELECT i FROM Investor i WHERE lower(i.firstName) LIKE lower(concat('%', :searchTerm, '%')) OR lower(i.lastName) LIKE lower(concat('%', :searchTerm, '%'))")
    Page<Investor> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<Investor> findByAccountId(UUID accountId);
}
