package com.br.nobilesol.controller;

import com.br.nobilesol.dto.account.AccountResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.mapper.AccountMapper;
import com.br.nobilesol.repository.AccountRepository;
import com.br.nobilesol.service.impl.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {


    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getCurrentAccount() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Account currentAccount = (Account) securityContext.getAuthentication().getPrincipal();
        return ResponseEntity.ok(accountService.getCurrentAccount(currentAccount));
    }
}
