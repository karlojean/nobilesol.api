package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.project.CreateProjectRequestDTO;
import com.br.nobilesol.dto.project.ProjectResponseDTO;
import com.br.nobilesol.dto.project.UpdateProjectRequestDTO;
import com.br.nobilesol.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "plants", ignore = true)
  Project toEntity(CreateProjectRequestDTO createProjectRequestDTO);

  ProjectResponseDTO toResponseDTO(Project project);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "plants", ignore = true)
  void updateProjectFromDTO(UpdateProjectRequestDTO updateDto, @MappingTarget Project project);
}