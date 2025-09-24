package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.dto.employee.UpdateEmployeeRequestDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.event.EmployeeCreatedEvent;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.mapper.EmployeeMapper;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.service.EmailService;
import com.br.nobilesol.utils.RandomPasswordGenerator;
import com.br.nobilesol.validation.validators.EmployeeValidator;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;
    private final EmployeeValidator employeeValidator;

    public EmployeeService(EmployeeMapper employeeMapper,
            EmployeeRepository employeeRepository,
            AccountService accountService,
            EmailService emailService,
            ApplicationEventPublisher eventPublisher,
            EmployeeValidator employeeValidator) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.accountService = accountService;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
        this.employeeValidator = employeeValidator;
    }

    @Transactional
    public EmployeeResponseDTO create(CreateEmployeeRequestDTO req) {
        employeeValidator.validateForCreation(req);

        String tempPassword = RandomPasswordGenerator.generatePassword(10);

        Account account = accountService.createAccount(
                req.account(),
                AccountRole.EMPLOYEE,
                tempPassword);

        Employee employee = employeeMapper.toEntity(req);
        employee.setAccount(account);
        account.setEmployee(employee);

        Employee created = employeeRepository.save(employee);

        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                created.getName(),
                created.getAccount().getEmail(),
                tempPassword);
        eventPublisher.publishEvent(event);

        // TODO: usar emailService para disparar credenciais, se/ quando implementar
        // emailService.sendEmployeeWelcome(created.getAccount().getEmail(),
        // created.getName(), tempPassword);

        return employeeMapper.toResponseDTO(created);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID id, UpdateEmployeeRequestDTO dto) {

        Employee existing = getEntityById(id);
        employeeValidator.validateForUpdate(existing, dto);

        employeeMapper.updateEmployeeFromDTO(dto, existing);

        Employee saved = employeeRepository.save(existing);

        return employeeMapper.toResponseDTO(saved);
    }

    @Transactional
    public PageResponseDTO<EmployeeResponseDTO> getAll(String filter, Pageable pageable) {
        Page<Employee> page = employeeRepository.search(filter, pageable);
        Page<EmployeeResponseDTO> dtoPage = page.map(employeeMapper::toResponseDTO);
        return PageResponseDTO.from(dtoPage);
    }

    @Transactional
    public EmployeeResponseDTO getById(UUID id) {
        Employee employee = getEntityById(id);
        return employeeMapper.toResponseDTO(employee);
    }

    public Employee getEntityById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NobileSolApiException(
                        "Funcionário não encontrado com ID: " + id, HttpStatus.NOT_FOUND));
    }
}
