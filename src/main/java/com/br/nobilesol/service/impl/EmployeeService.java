package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.event.EmployeeCreatedEvent;
import com.br.nobilesol.mapper.EmployeeMapper;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.service.EmailService;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    public EmployeeService(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository, AccountService accountService, EmailService emailService, ApplicationEventPublisher eventPublisher) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.accountService = accountService;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public EmployeeResponseDTO create(CreateEmployeeRequestDTO createEmployeeRequest) {
        String temporaryPassword = RandomPasswordGenerator.generatePassword(10);
        Account account = accountService.createAccount(
                createEmployeeRequest.account(),
                AccountRole.EMPLOYEE,
                temporaryPassword
        );

        Employee employee = employeeMapper.toEntity(createEmployeeRequest);
        employee.setAccount(account);
        account.setEmployee(employee);

        Employee createdEmployee = employeeRepository.save(employee);

        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                createdEmployee.getAccount().getName(),
                createdEmployee.getAccount().getEmail(),
                temporaryPassword
        );
        eventPublisher.publishEvent(event);

        return employeeMapper.toResponseDTO(createdEmployee);
    }
}
