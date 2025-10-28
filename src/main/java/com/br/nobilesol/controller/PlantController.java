package com.br.nobilesol.controller;

import com.br.nobilesol.dto.plant.CreatePlantRequestDTO;
import com.br.nobilesol.dto.plant.PlantResponseDTO;
import com.br.nobilesol.dto.plant.UpdatePlantRequestDTO;
import com.br.nobilesol.service.impl.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Plant", description = "Endpoints for managing plants")
@RestController
@RequestMapping("/projects/{projectId}/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @Operation(operationId = "Create a new plant in a project")
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PlantResponseDTO> create(
            @PathVariable UUID projectId,
            @RequestBody @Valid CreatePlantRequestDTO createPlantRequestDTO) {
        PlantResponseDTO response = plantService.create(projectId, createPlantRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(operationId = "Update an existing plant in a project")
    @PutMapping("/{plantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PlantResponseDTO> update(
            @PathVariable UUID projectId,
            @PathVariable UUID plantId,
            @RequestBody @Valid UpdatePlantRequestDTO updatePlantRequestDTO) {
        PlantResponseDTO response = plantService.update(projectId, plantId, updatePlantRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(operationId = "Get plant by ID in a project")
    @GetMapping("/{plantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PlantResponseDTO> getById(
            @PathVariable UUID projectId,
            @PathVariable UUID plantId) {
        PlantResponseDTO response = plantService.getById(projectId, plantId);
        return ResponseEntity.ok(response);
    }

    @Operation(operationId = "Get all plants in a project")
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<PlantResponseDTO>> getAllByProject(@PathVariable UUID projectId) {
        List<PlantResponseDTO> response = plantService.getAllByProject(projectId);
        return ResponseEntity.ok(response);
    }

    @Operation(operationId = "Delete a plant by ID in a project")
    @DeleteMapping("/{plantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> delete(
            @PathVariable UUID projectId,
            @PathVariable UUID plantId) {
        plantService.delete(projectId, plantId);
        return ResponseEntity.noContent().build();
    }
}