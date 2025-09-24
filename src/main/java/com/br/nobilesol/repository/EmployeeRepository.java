package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByAccountId(UUID accountId);

    @Query("""
            SELECT e
            FROM Employee e
            WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(e.department) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    Page<Employee> search(@Param("term") String term, Pageable pageable);

    long countByAdminTrue();
}
