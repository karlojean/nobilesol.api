package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.dto.employee.UpdateEmployeeRequestDTO;
import com.br.nobilesol.dto.investor.UpdateInvestorRequestDTO;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.Investor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { AccountMapper.class })
public interface EmployeeMapper {

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "admin", source = "isAdmin")
    Employee toEntity(CreateEmployeeRequestDTO createEmployeeRequestDTO);

    EmployeeResponseDTO toResponseDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "admin", ignore = true)
    void updateEmployeeFromDTO(UpdateEmployeeRequestDTO updateDto, @MappingTarget Employee employee);
}
