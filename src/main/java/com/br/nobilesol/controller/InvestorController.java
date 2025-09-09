package com.br.nobilesol.controller;

import com.br.nobilesol.dto.PageResponseDTO;
import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.dto.investor.UpdateInvestorRequestDTO;
import com.br.nobilesol.service.impl.InvestorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        return new ResponseEntity<>(investorService.create(createInvestorRequestDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<InvestorResponseDTO> updateInvestor(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInvestorRequestDTO request) {

        InvestorResponseDTO response = investorService.updateInvestor(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PageResponseDTO<InvestorResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String filter
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(investorService.getAll(filter, pageable));
    }
}
