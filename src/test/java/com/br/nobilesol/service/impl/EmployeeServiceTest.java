package com.br.nobilesol.service.impl;


import com.br.nobilesol.dto.account.AccountRequestDTO;
import com.br.nobilesol.dto.account.AccountResponseDTO;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private EmailService emailService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldCreateEmployeeSuccessfully() {
        try (MockedStatic<RandomPasswordGenerator> mockedStatic = mockStatic(RandomPasswordGenerator.class)) {

            String generatedPassword = "ABC1234567";
            mockedStatic.when(() -> RandomPasswordGenerator.generatePassword(10))
                    .thenReturn(generatedPassword);


            AccountRequestDTO accountRequestDTO = mock(AccountRequestDTO.class);
            CreateEmployeeRequestDTO requestDTO = mock(CreateEmployeeRequestDTO.class);
            when(requestDTO.account()).thenReturn(accountRequestDTO);

            Account account = new Account();
            account.setEmail("test@example.com");

            Employee employee = new Employee();
            employee.setFirstName("John");

            Employee savedEmployee = new Employee();
            savedEmployee.setFirstName("John");
            savedEmployee.setAccount(account);

            AccountResponseDTO accountResponseDTO = new AccountResponseDTO(
                    UUID.randomUUID(),
                    "John",
                    "john.doe@gmail.com",
                    AccountRole.EMPLOYEE
            );

            EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                    UUID.randomUUID(),
                    "John",
                    "Doe",
                    "Compras",
                    accountResponseDTO
            );

            when(accountService.createAccount(accountRequestDTO, AccountRole.EMPLOYEE, generatedPassword))
                    .thenReturn(account);
            when(employeeMapper.toEntity(requestDTO)).thenReturn(employee);
            when(employeeRepository.save(employee)).thenReturn(savedEmployee);
            when(employeeMapper.toResponseDTO(savedEmployee)).thenReturn(responseDTO);

            EmployeeResponseDTO result = employeeService.create(requestDTO);

            assertNotNull(result);
            assertEquals("John", result.firstName());
            assertEquals("john.doe@gmail.com", result.account().email());

            verify(accountService).createAccount(accountRequestDTO, AccountRole.EMPLOYEE, generatedPassword);
            verify(employeeMapper).toEntity(requestDTO);
            verify(employeeRepository).save(employee);
            verify(employeeMapper).toResponseDTO(savedEmployee);

            ArgumentCaptor<EmployeeCreatedEvent> eventCaptor = ArgumentCaptor.forClass(EmployeeCreatedEvent.class);
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            EmployeeCreatedEvent publishedEvent = eventCaptor.getValue();
            assertEquals("John", publishedEvent.name());
            assertEquals("test@example.com", publishedEvent.email());
            assertEquals(generatedPassword, publishedEvent.temporaryPassword());
        }
    }
}


