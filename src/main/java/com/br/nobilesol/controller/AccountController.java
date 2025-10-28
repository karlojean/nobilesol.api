package com.br.nobilesol.controller;

import com.br.nobilesol.dto.account.CurrentAccountResponseDTO;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.service.impl.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Account", description = "Endpoints for managing accounts")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(operationId = "Get current authenticated account")
    @GetMapping("/me")
    public ResponseEntity<CurrentAccountResponseDTO> getCurrentAccount() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Account currentAccount = (Account) securityContext.getAuthentication().getPrincipal();
        return ResponseEntity.ok(accountService.getCurrentAccount(currentAccount));
    }
}
