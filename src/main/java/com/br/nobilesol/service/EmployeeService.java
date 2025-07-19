package com.br.nobilesol.service;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.mapper.EmployeeMapper;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;

    public EmployeeService(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository, AccountService accountService) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.accountService = accountService;
    }

    @Transactional
    public EmployeeResponseDTO create(CreateEmployeeRequestDTO createEmployeeRequest) {
        String password = RandomPasswordGenerator.generatePassword(10);
        Account account = accountService.createAccount(
                createEmployeeRequest.account(),
                AccountRole.EMPLOYEE,
                password
        );

        Employee employee = employeeMapper.toEntity(createEmployeeRequest);
        employee.setAccount(account);
        account.setEmployee(employee);

        // Quando implementado, será enviado um email com a senha default
        System.out.println("Senha do usuário" + password);

        return employeeMapper.toResponseDTO(employeeRepository.save(employee));
    }
}
