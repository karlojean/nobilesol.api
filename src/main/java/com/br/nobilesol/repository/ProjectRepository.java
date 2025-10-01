package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

  @Query("""
      SELECT p
      FROM Project p
      WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
         OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
         OR LOWER(p.utilityCompany) LIKE LOWER(CONCAT('%', :term, '%'))
      """)
  Page<Project> search(@Param("term") String term, Pageable pageable);
}