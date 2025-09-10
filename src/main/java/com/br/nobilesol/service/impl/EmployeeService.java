package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.event.EmployeeCreatedEvent;
import com.br.nobilesol.mapper.EmployeeMapper;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.service.EmailService;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    public EmployeeService(EmployeeMapper employeeMapper,
                           EmployeeRepository employeeRepository,
                           AccountService accountService,
                           EmailService emailService,
                           ApplicationEventPublisher eventPublisher) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.accountService = accountService;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public EmployeeResponseDTO create(CreateEmployeeRequestDTO req) {
        String tempPassword = RandomPasswordGenerator.generatePassword(10);
        Account account = accountService.createAccount(
                req.account(),
                AccountRole.EMPLOYEE,
                tempPassword
        );

        Employee employee = employeeMapper.toEntity(req);
        employee.setAccount(account);
        account.setEmployee(employee);

        Employee created = employeeRepository.save(employee);

        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                created.getName(),
                created.getAccount().getEmail(),
                tempPassword
        );
        eventPublisher.publishEvent(event);

        // TODO: usar emailService para disparar credenciais, se/ quando implementar
        // emailService.sendEmployeeWelcome(created.getAccount().getEmail(), created.getName(), tempPassword);

        return employeeMapper.toResponseDTO(created);
    }
}
