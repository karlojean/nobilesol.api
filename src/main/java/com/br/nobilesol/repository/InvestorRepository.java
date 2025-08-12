package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Investor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvestorRepository extends JpaRepository<Investor, UUID> {
    Page<Investor> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
}
