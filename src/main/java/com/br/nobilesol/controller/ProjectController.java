package com.br.nobilesol.controller;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.project.CreateProjectRequestDTO;
import com.br.nobilesol.dto.project.ProjectResponseDTO;
import com.br.nobilesol.dto.project.UpdateProjectRequestDTO;
import com.br.nobilesol.service.impl.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ProjectResponseDTO> create(
      @RequestBody @Valid CreateProjectRequestDTO createProjectRequestDTO) {
    ProjectResponseDTO response = projectService.create(createProjectRequestDTO);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ProjectResponseDTO> update(
      @PathVariable UUID id,
      @RequestBody @Valid UpdateProjectRequestDTO updateProjectRequestDTO) {
    ProjectResponseDTO response = projectService.update(id, updateProjectRequestDTO);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ProjectResponseDTO> getById(@PathVariable UUID id) {
    ProjectResponseDTO response = projectService.getById(id);
    return ResponseEntity.ok(response);
  }

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

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    projectService.delete(id);
    return ResponseEntity.noContent().build();
  }
}