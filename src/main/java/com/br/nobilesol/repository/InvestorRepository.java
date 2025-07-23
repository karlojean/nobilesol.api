package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvestorRepository extends JpaRepository<Investor, UUID> {
}
