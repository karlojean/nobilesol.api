package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import com.br.nobilesol.dto.account.CurrentAccountResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.AccountRepository;
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

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
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

    public CurrentAccountResponseDTO getCurrentAccount(Account account) {
        String displayName = getDisplayName(account);

        return new CurrentAccountResponseDTO(
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

    public String getDisplayName(Account account) {
        return switch (account.getRole()) {
            case EMPLOYEE -> account.getEmployee().getDisplayName();
            case INVESTOR -> account.getInvestor().getDisplayName();
        };
    }

}
