package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.plant.CreatePlantRequestDTO;
import com.br.nobilesol.dto.plant.PlantResponseDTO;
import com.br.nobilesol.dto.plant.UpdatePlantRequestDTO;
import com.br.nobilesol.entity.Plant;
import com.br.nobilesol.entity.Project;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.mapper.PlantMapper;
import com.br.nobilesol.repository.PlantRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlantService {

  private final PlantMapper plantMapper;
  private final PlantRepository plantRepository;
  private final ProjectService projectService;

  public PlantService(PlantMapper plantMapper, PlantRepository plantRepository, ProjectService projectService) {
    this.plantMapper = plantMapper;
    this.plantRepository = plantRepository;
    this.projectService = projectService;
  }

  @Transactional
  public PlantResponseDTO create(UUID projectId, CreatePlantRequestDTO createPlantRequestDTO) {
    Project project = projectService.getProjectEntity(projectId);

    if (!projectId.equals(createPlantRequestDTO.projectId())) {
      throw new NobileSolApiException(
          "O ID do projeto na URL não corresponde ao ID do projeto no corpo da requisição",
          HttpStatus.BAD_REQUEST);
    }

    Plant plant = plantMapper.toEntity(createPlantRequestDTO);
    plant.setProject(project);

    Plant savedPlant = plantRepository.save(plant);
    return plantMapper.toResponseDTO(savedPlant);
  }

  @Transactional
  public PlantResponseDTO update(UUID projectId, UUID plantId, UpdatePlantRequestDTO updatePlantRequestDTO) {
    Plant plant = findPlantByIdAndProject(plantId, projectId);

    plantMapper.updatePlantFromDTO(updatePlantRequestDTO, plant);

    Plant updatedPlant = plantRepository.save(plant);
    return plantMapper.toResponseDTO(updatedPlant);
  }

  public PlantResponseDTO getById(UUID projectId, UUID plantId) {
    Plant plant = findPlantByIdAndProject(plantId, projectId);
    return plantMapper.toResponseDTO(plant);// // TODO Validar se o projeto existe Validar se o projeto existe
  }

  public List<PlantResponseDTO> getAllByProject(UUID projectId) {
    // TODO Validar se o projeto existe
    projectService.getProjectEntity(projectId);

    List<Plant> plants = plantRepository.findByProjectId(projectId);
    return plants.stream()
        .map(plantMapper::toResponseDTO)
        .toList();
  }

  @Transactional
  public void delete(UUID projectId, UUID plantId) {
    Plant plant = findPlantByIdAndProject(plantId, projectId);
    plantRepository.delete(plant);
  }

  private Plant findPlantByIdAndProject(UUID plantId, UUID projectId) {
    Plant plant = plantRepository.findById(plantId)
        .orElseThrow(() -> new NobileSolApiException(
            "Planta não encontrada com ID: " + plantId,
            HttpStatus.NOT_FOUND));

    if (!plant.getProject().getId().equals(projectId)) {
      throw new NobileSolApiException(
          "Planta não pertence ao projeto especificado",
          HttpStatus.BAD_REQUEST);
    }

    return plant;
  }
}