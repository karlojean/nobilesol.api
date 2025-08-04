package com.br.nobilesol.controller;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.service.impl.InvestorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investor")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<InvestorResponseDTO> create(@RequestBody @Valid CreateInvestorRequestDTO createInvestorRequestDTO) {
        return ResponseEntity.ok(investorService.create(createInvestorRequestDTO));
    }
}
