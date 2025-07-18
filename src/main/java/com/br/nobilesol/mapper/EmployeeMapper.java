package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EmployeeMapper {
    @Mapping(target = "user", ignore = true)
    Employee toEntity(CreateEmployeeRequestDTO createEmployeeRequestDTO);

    EmployeeResponseDTO toResponseDTO(Employee employee);
}
