package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlantRepository extends JpaRepository<Plant, UUID> {

  List<Plant> findByProjectId(UUID projectId);

  Page<Plant> findByProjectId(UUID projectId, Pageable pageable);

  @Query("""
      SELECT p
      FROM Plant p
      WHERE p.project.id = :projectId
      AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
         OR LOWER(p.type) LIKE LOWER(CONCAT('%', :term, '%'))
         OR LOWER(p.status) LIKE LOWER(CONCAT('%', :term, '%')))
      """)
  Page<Plant> searchByProject(@Param("projectId") UUID projectId, @Param("term") String term, Pageable pageable);
}