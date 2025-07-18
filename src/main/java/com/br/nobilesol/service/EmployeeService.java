package com.br.nobilesol.service;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.entity.enums.UserRole;
import com.br.nobilesol.mapper.EmployeeMapper;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    public EmployeeService(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository,UserService userService) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.userService = userService;
    }

    @Transactional
    public EmployeeResponseDTO create(CreateEmployeeRequestDTO createEmployeeRequest) {
        String password = RandomPasswordGenerator.generatePassword(10);
        User user = userService.createUser(
                createEmployeeRequest.userCredentials(),
                UserRole.EMPLOYEE,
                password
        );

        Employee employee = employeeMapper.toEntity(createEmployeeRequest);
        employee.setUser(user);

        // Quando implementado, será enviado um email com a senha default
        System.out.println("Senha do usuário" + password);

        return employeeMapper.toResponseDTO(employeeRepository.save(employee));
    }
}
