package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import com.br.nobilesol.dto.account.AccountResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.Investor;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.AccountRepository;
import com.br.nobilesol.repository.EmployeeRepository;
import com.br.nobilesol.repository.InvestorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvestorRepository investorRepository;
    private final EmployeeRepository employeeRepository;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, InvestorRepository investorRepository, EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.investorRepository = investorRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Account createAccount(AccountRequestDTO accountCredentialsRequest, AccountRole role, String password) {
        if (accountRepository.existsByEmail(accountCredentialsRequest.email())) {
            throw new NobileSolApiException("Email já está a ser utilizado por outra conta.", HttpStatus.BAD_REQUEST);
        }

        Account newAccount = new Account();
        newAccount.setEmail(accountCredentialsRequest.email());
        newAccount.setPasswordHash(passwordEncoder.encode(password));
        newAccount.setRole(role);
        newAccount.setIsActive(true);

        return accountRepository.save(newAccount);
    }

    public AccountResponseDTO getCurrentAccount(Account account) {
        String displayName = getDisplayName(account.getId(), account.getRole());

        return new AccountResponseDTO(
                account.getId(),
                displayName,
                account.getEmail(),
                account.getRole()
        );
    }

    public void changePassword(Account account, String password) {
        account.setPasswordHash(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    public Account findEntityByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Usuário com o email: " + email + ", não encontrado")
        );
    }

    public boolean accountExistsWithEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public String getDisplayName(UUID accountId, AccountRole role) {
        return switch (role) {
            case INVESTOR -> investorRepository.findByAccountId(accountId)
                    .map(Investor::getFirstName)
                    .orElse(null);
            case EMPLOYEE -> employeeRepository.findByAccountId(accountId)
                    .map(Employee::getFirstName)
                    .orElse(null);
            default -> null;
        };
    }
}
