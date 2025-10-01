package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.plant.CreatePlantRequestDTO;
import com.br.nobilesol.dto.plant.PlantResponseDTO;
import com.br.nobilesol.dto.plant.UpdatePlantRequestDTO;
import com.br.nobilesol.entity.Plant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlantMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "investors", ignore = true)
  Plant toEntity(CreatePlantRequestDTO createPlantRequestDTO);

  PlantResponseDTO toResponseDTO(Plant plant);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "investors", ignore = true)
  void updatePlantFromDTO(UpdatePlantRequestDTO updateDto, @MappingTarget Plant plant);
}