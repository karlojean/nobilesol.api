package com.br.nobilesol.controller;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.dto.employee.UpdateEmployeeRequestDTO;
import com.br.nobilesol.service.impl.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Employee", description = "Endpoints for managing employees")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(operationId = "Create a new employee")
    @PostMapping()
    @PreAuthorize("hasAuthority('PERMISSION_EMPLOYEE_MANAGEMENT')")
    public ResponseEntity<EmployeeResponseDTO> create(
            @RequestBody @Valid CreateEmployeeRequestDTO createEmployeeRequestDTO) {
        return new ResponseEntity<>(employeeService.create(createEmployeeRequestDTO), HttpStatus.CREATED);
    }

    @Operation(operationId = "Update an existing employee")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_EMPLOYEE_MANAGEMENT')")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmployeeRequestDTO request) {

        EmployeeResponseDTO response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(operationId = "Get all employees")
    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_EMPLOYEE_MANAGEMENT')")
    public ResponseEntity<PageResponseDTO<EmployeeResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String filter) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(employeeService.getAll(filter, pageable));
    }

    @Operation(operationId = "Get employee by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_EMPLOYEE_MANAGEMENT')")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }
}
