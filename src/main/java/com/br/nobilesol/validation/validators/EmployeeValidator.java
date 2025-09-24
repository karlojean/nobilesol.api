package com.br.nobilesol.validation.validators;

import com.br.nobilesol.dto.employee.CreateEmployeeRequestDTO;
import com.br.nobilesol.dto.employee.UpdateEmployeeRequestDTO;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.AccountRepository;
import com.br.nobilesol.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

  private final AccountRepository accountRepository;
  private final EmployeeRepository employeeRepository;

  public void validateForCreation(CreateEmployeeRequestDTO request) {
    if (!StringUtils.hasText(request.name())) {
      throw new NobileSolApiException("Nome e obrigatório.", HttpStatus.BAD_REQUEST);
    }

    if (!StringUtils.hasText(request.department())) {
      throw new NobileSolApiException("Departamento é obrigatório.", HttpStatus.BAD_REQUEST);
    }

    String email = request.account().email().trim().toLowerCase();
    if (accountRepository.existsByEmail(email)) {
      throw new NobileSolApiException("Email já está em uso.", HttpStatus.CONFLICT);
    }
  }

  public void validateForUpdate(Employee existingEmployee, UpdateEmployeeRequestDTO request) {
    if (request.name() != null && !StringUtils.hasText(request.name())) {
      throw new NobileSolApiException("Nome não pode ser vazio.", HttpStatus.BAD_REQUEST);
    }

    if (request.department() != null && !StringUtils.hasText(request.department())) {
      throw new NobileSolApiException("Departamento não pode ser vazio.", HttpStatus.BAD_REQUEST);
    }

    boolean removingAdmin = existingEmployee.isAdmin() && !request.isAdmin();

    if(removingAdmin && employeeRepository.countByAdminTrue() <= 1) {
      throw new NobileSolApiException("O Sistema deve conter pelo menos um usuário administrador.", HttpStatus.BAD_REQUEST);
    }
  }
}