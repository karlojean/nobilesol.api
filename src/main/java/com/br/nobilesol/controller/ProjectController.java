package com.br.nobilesol.controller;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.project.CreateProjectRequestDTO;
import com.br.nobilesol.dto.project.ProjectResponseDTO;
import com.br.nobilesol.dto.project.UpdateProjectRequestDTO;
import com.br.nobilesol.service.impl.ProjectService;
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

@Tag(name = "Project", description = "Endpoints for managing projects")
@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @Operation(operationId = "Create a new project")
  @PostMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ProjectResponseDTO> create(
      @RequestBody @Valid CreateProjectRequestDTO createProjectRequestDTO) {
    ProjectResponseDTO response = projectService.create(createProjectRequestDTO);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Operation(operationId = "Update an existing project")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ProjectResponseDTO> update(
      @PathVariable UUID id,
      @RequestBody @Valid UpdateProjectRequestDTO updateProjectRequestDTO) {
    ProjectResponseDTO response = projectService.update(id, updateProjectRequestDTO);
    return ResponseEntity.ok(response);
  }

  @Operation(operationId = "Get project by ID")
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ProjectResponseDTO> getById(@PathVariable UUID id) {
    ProjectResponseDTO response = projectService.getById(id);
    return ResponseEntity.ok(response);
  }

  @Operation(operationId = "Get all projects")
  @GetMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<PageResponseDTO<ProjectResponseDTO>> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "") String filter) {
    Pageable pageable = PageRequest.of(page, size);
    PageResponseDTO<ProjectResponseDTO> response = projectService.getAll(filter, pageable);
    return ResponseEntity.ok(response);
  }

  @Operation(operationId = "Delete a project by ID")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    projectService.delete(id);
    return ResponseEntity.noContent().build();
  }
}