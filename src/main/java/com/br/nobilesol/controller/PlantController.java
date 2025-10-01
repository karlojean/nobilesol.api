package com.br.nobilesol.controller;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.plant.CreatePlantRequestDTO;
import com.br.nobilesol.dto.plant.PlantResponseDTO;
import com.br.nobilesol.dto.plant.UpdatePlantRequestDTO;
import com.br.nobilesol.service.impl.PlantService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PlantResponseDTO> create(
            @PathVariable UUID projectId,
            @RequestBody @Valid CreatePlantRequestDTO createPlantRequestDTO) {
        PlantResponseDTO response = plantService.create(projectId, createPlantRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{plantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PlantResponseDTO> update(
            @PathVariable UUID projectId,
            @PathVariable UUID plantId,
            @RequestBody @Valid UpdatePlantRequestDTO updatePlantRequestDTO) {
        PlantResponseDTO response = plantService.update(projectId, plantId, updatePlantRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{plantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PlantResponseDTO> getById(
            @PathVariable UUID projectId,
            @PathVariable UUID plantId) {
        PlantResponseDTO response = plantService.getById(projectId, plantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<PlantResponseDTO>> getAllByProject(@PathVariable UUID projectId) {
        List<PlantResponseDTO> response = plantService.getAllByProject(projectId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{plantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> delete(
            @PathVariable UUID projectId,
            @PathVariable UUID plantId) {
        plantService.delete(projectId, plantId);
        return ResponseEntity.noContent().build();
    }
}