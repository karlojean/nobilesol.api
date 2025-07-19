package com.br.nobilesol.controller;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> create(@RequestBody @Valid CreateEmployeeRequestDTO createEmployeeRequestDTO) {
        return ResponseEntity.ok(employeeService.create(createEmployeeRequestDTO));
    }
}
