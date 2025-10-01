package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.project.CreateProjectRequestDTO;
import com.br.nobilesol.dto.project.ProjectResponseDTO;
import com.br.nobilesol.dto.project.UpdateProjectRequestDTO;
import com.br.nobilesol.entity.Project;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.mapper.ProjectMapper;
import com.br.nobilesol.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class ProjectService {

  private final ProjectMapper projectMapper;
  private final ProjectRepository projectRepository;

  public ProjectService(ProjectMapper projectMapper, ProjectRepository projectRepository) {
    this.projectMapper = projectMapper;
    this.projectRepository = projectRepository;
  }

  @Transactional
  public ProjectResponseDTO create(CreateProjectRequestDTO createProjectRequestDTO) {
    Project project = projectMapper.toEntity(createProjectRequestDTO);

    Project savedProject = projectRepository.save(project);
    return projectMapper.toResponseDTO(savedProject);
  }

  @Transactional
  public ProjectResponseDTO update(UUID id, UpdateProjectRequestDTO updateProjectRequestDTO) {
    Project project = findProjectById(id);

    projectMapper.updateProjectFromDTO(updateProjectRequestDTO, project);
    project.setUpdatedAt(Instant.now());

    Project updatedProject = projectRepository.save(project);
    return projectMapper.toResponseDTO(updatedProject);
  }

  public ProjectResponseDTO getById(UUID id) {
    Project project = findProjectById(id);
    return projectMapper.toResponseDTO(project);
  }

  public PageResponseDTO<ProjectResponseDTO> getAll(String filter, Pageable pageable) {
    Page<Project> projectPage = projectRepository.search(filter.trim(), pageable);

    Page<ProjectResponseDTO> responseDTOPage = projectPage.map(projectMapper::toResponseDTO);

    return PageResponseDTO.from(responseDTOPage);
  }

  @Transactional
  public void delete(UUID id) {
    Project project = findProjectById(id);
    projectRepository.delete(project);
  }

  private Project findProjectById(UUID id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new NobileSolApiException(
            "Projeto não encontrado com ID: " + id,
            HttpStatus.NOT_FOUND));
  }

  // Método auxiliar para outros serviços
  public Project getProjectEntity(UUID id) {
    return findProjectById(id);
  }
}