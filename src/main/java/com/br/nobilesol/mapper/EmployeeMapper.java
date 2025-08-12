package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "admin", source = "isAdmin")
    Employee toEntity(CreateEmployeeRequestDTO createEmployeeRequestDTO);

    @Mapping(target = "account", expression = "java(new AccountResponseDTO(employee.getAccount().getId(), employee.getFirstName(), employee.getAccount().getEmail(),  employee.getAccount().getRole()))")
    EmployeeResponseDTO toResponseDTO(Employee employee);
}
