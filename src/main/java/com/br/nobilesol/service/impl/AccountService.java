package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import com.br.nobilesol.dto.account.CurrentAccountResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.Employee;
import com.br.nobilesol.entity.Investor;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
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
    public Account createAccount(AccountRequestDTO req, AccountRole role, String rawPassword) {
        String email = normalizeEmail(req.email());

        if (accountRepository.existsByEmail(email)) {
            throw new NobileSolApiException("Email já está sendo utilizado por outra conta.", HttpStatus.BAD_REQUEST);
        }

        Account acc = new Account();
        acc.setEmail(email);
        acc.setPasswordHash(passwordEncoder.encode(rawPassword));
        acc.setRole(role != null ? role : AccountRole.INVESTOR);
        acc.setActive(true);

        return accountRepository.save(acc);
    }

    public CurrentAccountResponseDTO getCurrentAccount(Account account) {
        String displayName = resolveDisplayName(account);
        return new CurrentAccountResponseDTO(
                account.getId(),
                displayName,
                account.getEmail(),
                account.getRole()
        );
    }

    @Transactional
    public void changePassword(Account account, String newRawPassword) {
        account.setPasswordHash(passwordEncoder.encode(newRawPassword));
        accountRepository.save(account);
    }

    public Account findEntityByEmail(String anyCaseEmail) {
        String email = normalizeEmail(anyCaseEmail);
        return accountRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Usuário com o email: " + email + " não encontrado")
        );
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public String resolveDisplayName(Account account) {
        Employee employee = account.getEmployee();
        if (employee != null && employee.getName() != null && !employee.getName().isBlank()) {
            return employee.getName();
        }

        Investor investor = account.getInvestor();
        if (investor != null) {
            String invName = investor.getDisplayName();
            if (invName != null && !invName.isBlank()) return invName;
        }

        return account.getEmail();
    }
}
